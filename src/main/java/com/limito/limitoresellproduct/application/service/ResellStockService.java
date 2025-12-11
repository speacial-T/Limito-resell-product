package com.limito.limitoresellproduct.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.model.Stocks;
import com.limito.limitoresellproduct.domain.repository.ResellStockRepository;
import com.limito.limitoresellproduct.domain.repository.StockInMemoryRepository;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.request.StockReduceRequest;
import com.limito.limitoresellproduct.presentation.dto.response.StockCancelResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockReduceResponseV1;

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

	public void reserveStocks(List<UUID> stockIds) {
		for (UUID stockId : stockIds) {
			reserveStock(stockId);
		}
	}

	private void reserveStock(UUID stockId) {
		String key = REDIS_STOCK_PREFIX_KEY + stockId;

		if (resellStockRepository.findById(stockId) == null) {
			throw new AppException(ProductErrorCode.WRONG_STOCK_ID);
		}

		if (stockInMemoryRepository.get(key) != null) {
			throw new AppException(ProductErrorCode.OUT_OF_STOCK);
		}

		stockInMemoryRepository.set(key, "1");
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

	@Transactional
	public StockCancelResponseV1 cancelStocks(List<UUID> stockIds) {
		ProductErrorCode failReason = null;

		for (UUID stockId : stockIds) {
			failReason = deleteReservedStock(stockId);
		}

		if (failReason == null) {
			return null;
		}

		return ProductMapper.toStockCancelResponseV1(failReason, stockIds);
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
			return ProductErrorCode.WRONG_STOCK_ID;
		}

		stockInMemoryRepository.delete(key);
		return null;
	}

	private ProductErrorCode deleteStock(UUID stockId) {
		Stock stock = resellStockRepository.findById(stockId);
		if (stock == null) {
			return ProductErrorCode.WRONG_STOCK_ID;
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
