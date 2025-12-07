package com.limito.limitoresellproduct.domain.repository;

import com.limito.limitoresellproduct.domain.model.Stock;

public interface ResellStockRepository {

	Stock saveStock(Stock stock);
}
