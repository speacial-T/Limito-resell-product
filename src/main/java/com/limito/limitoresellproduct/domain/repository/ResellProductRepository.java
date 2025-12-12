package com.limito.limitoresellproduct.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.limito.limitoresellproduct.domain.model.Product;

public interface ResellProductRepository {

	Product saveProduct(Product product);

	Product findById(UUID productId);

	Product findByIdForAllUser(UUID productId);

	Page<Product> findAllByCategoryIdForAllUser(UUID categoryId, Pageable pageable);
}
