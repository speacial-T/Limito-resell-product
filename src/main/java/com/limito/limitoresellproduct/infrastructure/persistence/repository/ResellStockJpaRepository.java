package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.limito.limitoresellproduct.domain.model.Stock;

public interface ResellStockJpaRepository extends JpaRepository<Stock, UUID> {
	List<Stock> findAllByOptionId(UUID optionId);
}
