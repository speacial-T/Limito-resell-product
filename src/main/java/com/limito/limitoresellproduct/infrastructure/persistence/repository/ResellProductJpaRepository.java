package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.limito.limitoresellproduct.domain.model.Product;

public interface ResellProductJpaRepository extends JpaRepository<Product, UUID> {
	Page<Product> findAllByCategoryId(UUID categoryId, Pageable pageable);
}
