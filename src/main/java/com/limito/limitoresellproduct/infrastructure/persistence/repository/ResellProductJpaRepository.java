package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.limito.limitoresellproduct.domain.model.Product;

public interface ResellProductJpaRepository extends JpaRepository<Product, UUID> {
}
