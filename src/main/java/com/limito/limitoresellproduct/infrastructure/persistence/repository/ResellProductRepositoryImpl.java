package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

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
	public Product findById(UUID productId) {
		return jpaRepository.findById(productId).orElse(null);
	}

	@Override
	public Page<Product> findAllByCategoryId(UUID categoryId, Pageable pageable) {
		return jpaRepository.findAllByCategoryId(categoryId, pageable);
	}

	@Override
	public Product findByIdOrElseThorw(UUID productId) {
		return jpaRepository.findById(productId).orElseThrow(() ->
			new AppException(ProductErrorCode.WRONG_PRODUCT_ID)
		);
	}
}
