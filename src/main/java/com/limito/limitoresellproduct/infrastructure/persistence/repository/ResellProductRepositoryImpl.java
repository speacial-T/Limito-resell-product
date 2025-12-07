package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import org.springframework.stereotype.Repository;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ResellProductRepositoryImpl implements ResellProductRepository {

	private final ResellProductJpaRepository jpaRepository;

	@Override
	public Product saveProduct(Product product) {
		return jpaRepository.save(product);
	}
}
