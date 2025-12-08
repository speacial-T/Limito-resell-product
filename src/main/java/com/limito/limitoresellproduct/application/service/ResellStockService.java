package com.limito.limitoresellproduct.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.model.Stocks;
import com.limito.limitoresellproduct.domain.repository.ResellStockRepository;
import com.limito.limitoresellproduct.domain.repository.StockInMemoryRepository;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.request.StockReduceRequest;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockReduceResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockReserveResponseV1;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResellStockService {

	private final ResellProductService productService;
	private final ResellStockRepository resellStockRepository;
	private final StockInMemoryRepository stockInMemoryRepository;

	private static final String REDIS_STOCK_PREFIX_KEY = "resell-stock:";

	public StockCreateResponseV1 createStock(@Valid StockCreateRequestV1 request) {
		Stock stock = ProductMapper.toEntity(request);

		// TODO optionID 존재 확인

		Stock savedStock = resellStockRepository.saveStock(stock);

		productService.changeMinimumPriceStock(
			request.getProductId(),
			savedStock.getOptionId(),
			savedStock.getId(),
			savedStock.getPrice()
		);

		return ProductMapper.toDto(savedStock);
	}

	public StockReserveResponseV1 reserveStocks(List<UUID> stockIds) {
		StockReserveResponseV1 result = null;
		for (UUID stockId : stockIds) {
			result = reserveStock(stockId);
		}
		if (result != null) {
			result.setStockIds(stockIds);
		}
		return result;
	}

	private StockReserveResponseV1 reserveStock(UUID stockId) {
		if (resellStockRepository.findById(stockId) == null) {
			return StockReserveResponseV1.builder()
				.errorCode("E001")
				.message("잘못된 리셀 재고 ID입니다.")
				.build();
		}

		if (stockInMemoryRepository.get(REDIS_STOCK_PREFIX_KEY + stockId) != null) {
			return StockReserveResponseV1.builder()
				.errorCode("E002")
				.message("재고 부족")
				.build();
		}

		stockInMemoryRepository.set(REDIS_STOCK_PREFIX_KEY + stockId, "1");
		return null;
	}

	@Transactional
	public StockReduceResponseV1 reduceStocks(@Valid List<StockReduceRequest> requests) {
		StockReduceResponseV1 result = null;
		ProductErrorCode failReason = null;

		for (StockReduceRequest request : requests) {
			failReason = reduceStock(request);
		}

		if (failReason != null) {
			List<UUID> stockIds = requests.stream()
				.map(StockReduceRequest::getStockId)
				.toList();
			result = ProductMapper.toStockReduceResponseV1(failReason, stockIds);
		}

		return result;
	}

	private ProductErrorCode reduceStock(StockReduceRequest request) {
		UUID stockId = request.getStockId();
		UUID optionId = request.getOptionId();
		UUID productId = request.getProductId();
		ProductErrorCode failReason = null;

		failReason = deleteReservedStock(stockId);
		if (failReason != null) {
			return failReason;
		}

		failReason = deleteStock(stockId);
		if (failReason != null) {
			return failReason;
		}

		refreshMinStockOfOption(productId, optionId);
		return failReason;
	}

	private ProductErrorCode deleteReservedStock(UUID stockId) {
		String key = REDIS_STOCK_PREFIX_KEY + stockId;

		String reservedStock = stockInMemoryRepository.get(key);
		if (reservedStock == null) {
			return ProductErrorCode.WRONG_ID;
		}

		stockInMemoryRepository.delete(key);
		return null;
	}

	private ProductErrorCode deleteStock(UUID stockId) {
		Stock stock = resellStockRepository.findById(stockId);
		if (stock == null) {
			return ProductErrorCode.WRONG_ID;
		}

		if (stock.isDeleted()) {
			return ProductErrorCode.OUT_OF_STOCK;
		}

		resellStockRepository.deleteStock(stock);
		return null;
	}

	private void refreshMinStockOfOption(UUID productId, UUID optionId) {
		List<Stock> stocks = resellStockRepository.findAllByOptionId(optionId);
		Stock minStock = Stocks.calculateMinStock(stocks);
		productService.changeMinimumPriceStock(productId, optionId, minStock.getId(), minStock.getPrice());
	}
}
