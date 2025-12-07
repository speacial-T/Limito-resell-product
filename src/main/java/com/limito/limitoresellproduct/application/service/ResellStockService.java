package com.limito.limitoresellproduct.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.repository.ResellStockRepository;
import com.limito.limitoresellproduct.domain.repository.StockInMemoryRepository;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockReserveResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResellStockService {

	private final ResellProductService productService;
	private final ResellStockRepository resellStockRepository;
	private final StockInMemoryRepository stockInMemoryRepository;

	private static final String REDIS_STOCK_PREFIX_KEY = "resell-stock";

	public StockCreateResponseV1 createStock(@Valid StockCreateRequestV1 request) {
		Stock stock = ProductMapper.toEntity(request);

		// TODO optionID 존재 확인

		Stock savedStock = resellStockRepository.saveStock(stock);

		productService.changeMinimumPriceStock(
			request.getProductId(),
			savedStock.getOptionId(),
			savedStock.getStockId(),
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
}
