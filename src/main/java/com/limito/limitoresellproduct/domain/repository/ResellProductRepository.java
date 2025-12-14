package com.limito.limitoresellproduct.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.limito.limitoresellproduct.domain.model.Product;

public interface ResellProductRepository {

	Product saveProduct(Product product);

	Product findById(UUID productId);

	Page<Product> findAllByCategoryId(UUID categoryId, Pageable pageable);

	Product findByIdOrElseThorw(UUID productId);
}
