package com.limito.limitoresellproduct.domain.repository;

import java.util.UUID;

import com.limito.limitoresellproduct.domain.model.Product;

public interface ResellProductRepository {

	Product saveProduct(Product product);

	Product findById(UUID productId);
}
