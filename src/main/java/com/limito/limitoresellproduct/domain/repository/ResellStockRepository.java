package com.limito.limitoresellproduct.domain.repository;

import java.util.List;
import java.util.UUID;

import com.limito.limitoresellproduct.domain.model.Stock;

public interface ResellStockRepository {

	Stock saveStock(Stock stock);

	Stock findById(UUID stockId);

	List<Stock> findAllByOptionId(UUID optionId);
}
