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
import com.limito.limitoresellproduct.presentation.dto.request.StockRollbackRequest;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;

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

	@Transactional
	public StockCreateResponseV1 createStock(@Valid StockCreateRequestV1 request) {
		Stock stock = ProductMapper.toEntity(request);

		productService.validateProductAndOption(request.getProductId(), request.getOptionId());

		Stock savedStock = resellStockRepository.saveStock(stock);

		refreshMinStockOfOption(request.getProductId(), request.getOptionId());

		return ProductMapper.toStockCreateResponseV1(savedStock);
	}

	public void reserveStocks(List<UUID> stockIds) {
		for (UUID stockId : stockIds) {
			reserveStock(stockId);
		}
	}

	private void reserveStock(UUID stockId) {
		String key = REDIS_STOCK_PREFIX_KEY + stockId;

		Stock stock = resellStockRepository.findById(stockId);
		if (stock == null) {
			throw new AppException(ProductErrorCode.WRONG_STOCK_ID);
		}
		stock.checkActive();

		if (stockInMemoryRepository.get(key) != null) {
			throw new AppException(ProductErrorCode.OUT_OF_STOCK);
		}

		stockInMemoryRepository.set(key, "1");
	}

	@Transactional
	public void reduceStocks(@Valid List<StockReduceRequest> requests) {
		for (StockReduceRequest request : requests) {
			reduceStock(request);
		}
	}

	@Transactional
	public void cancelStocks(List<UUID> stockIds) {
		for (UUID stockId : stockIds) {
			deleteReservedStock(stockId);
		}
	}

	private void reduceStock(StockReduceRequest request) {
		UUID stockId = request.getStockId();
		UUID optionId = request.getOptionId();
		UUID productId = request.getProductId();

		deleteReservedStock(stockId);
		sellStock(stockId);
		refreshMinStockOfOption(productId, optionId);
	}

	private void deleteReservedStock(UUID stockId) {
		String key = REDIS_STOCK_PREFIX_KEY + stockId;

		String reservedStock = stockInMemoryRepository.get(key);
		if (reservedStock == null) {
			throw new AppException(ProductErrorCode.WRONG_STOCK_ID);
		}

		stockInMemoryRepository.delete(key);
	}

	private void sellStock(UUID stockId) {
		Stock stock = resellStockRepository.findById(stockId);
		if (stock == null) {
			throw new AppException(ProductErrorCode.WRONG_STOCK_ID);
		}
		stock.checkActive();

		stock.changeSoldOutTo(true);
	}

	private void refreshMinStockOfOption(UUID productId, UUID optionId) {
		List<Stock> stocks = resellStockRepository.findAllByOptionId(optionId);
		Stock minStock = Stocks.calculateMinStock(stocks);
		productService.changeMinimumPriceStock(productId, optionId, minStock.getId(), minStock.getPrice());
	}

	@Transactional
	public void rollbackStocks(@Valid List<StockRollbackRequest> requests) {
		for (StockRollbackRequest request : requests) {
			rollbackStock(request);
		}
	}

	private void rollbackStock(StockRollbackRequest request) {
		Stock stock = resellStockRepository.findById(request.getStockId());
		if (stock == null) {
			throw new AppException(ProductErrorCode.WRONG_STOCK_ID);
		}

		if (!stock.isSoldOut()) {
			throw new AppException(ProductErrorCode.ALREADY_EXIST);
		}

		stock.changeSoldOutTo(false);
		refreshMinStockOfOption(request.getProductId(), request.getOptionId());
	}
}
