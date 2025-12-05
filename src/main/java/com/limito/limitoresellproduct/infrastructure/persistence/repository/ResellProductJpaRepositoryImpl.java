package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ResellProductJpaRepositoryImpl implements ResellProductRepository {

	private final ResellProductJpaRepository jpaRepository;

	@Override
	public Product save(Product product) {
		return jpaRepository.save(product);
	}

	private interface ResellProductJpaRepository extends JpaRepository<Product, UUID> {
	}
}
