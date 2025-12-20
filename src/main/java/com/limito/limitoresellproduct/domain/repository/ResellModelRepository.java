package com.limito.limitoresellproduct.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.limito.limitoresellproduct.domain.model.Model;

public interface ResellModelRepository {

	Model saveModel(Model model);

	Model findById(UUID modelId);

	Model findByIdForAllUser(UUID modelId);

	Page<Model> findAllByCategoryIdForAllUser(UUID categoryId, Pageable pageable);
}
