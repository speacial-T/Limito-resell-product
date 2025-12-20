package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.limito.limitoresellproduct.domain.model.Model;

public interface ResellModelJpaRepository extends JpaRepository<Model, UUID> {

	@Query("""
			SELECT m
			FROM Model m
			JOIN m.product p
			WHERE p.categoryId = :categoryId
			  AND m.deletedAt IS NULL
		""")
	Page<Model> findAllByCategoryIdForAllUser(@Param("categoryId") UUID categoryId, Pageable pageable);

	Optional<Model> findByModelIdAndDeletedAtIsNull(UUID modelId);
}
