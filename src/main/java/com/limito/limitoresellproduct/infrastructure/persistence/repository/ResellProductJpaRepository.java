package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.limito.limitoresellproduct.domain.model.Product;

public interface ResellProductJpaRepository extends JpaRepository<Product, UUID> {
	Page<Product> findAllByCategoryIdAndDeletedAtIsNull(UUID categoryId, Pageable pageable);

	Optional<Product> findByProductIdAndDeletedAtIsNull(UUID productId);
}
