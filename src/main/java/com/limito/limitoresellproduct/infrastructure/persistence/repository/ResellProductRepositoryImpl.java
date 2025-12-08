package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

	@Override
	public Product findByIdForAllUser(UUID productId) {
		return jpaRepository.findByProductIdAndDeletedAtIsNull(productId).orElse(null);
	}

	@Override
	public Page<Product> findAllByCategoryIdForAllUser(UUID categoryId, Pageable pageable) {
		return jpaRepository.findAllByCategoryIdAndDeletedAtIsNull(categoryId, pageable);
	}
}
