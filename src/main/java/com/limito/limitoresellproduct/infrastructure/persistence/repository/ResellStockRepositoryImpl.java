package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.repository.ResellStockRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ResellStockRepositoryImpl implements ResellStockRepository {

	private final ResellStockJpaRepository jpaRepository;

	@Override
	public Stock saveStock(Stock stock) {
		return jpaRepository.save(stock);
	}

	@Override
	public Stock findById(UUID stockId) {
		return jpaRepository.findById(stockId).orElse(null);
	}

	@Override
	public void deleteStock(Stock stock) {
		stock.delete();
	}

	@Override
	public List<Stock> findAllByOptionId(UUID optionId) {
		return jpaRepository.findAllByOptionId(optionId);
	}
}
