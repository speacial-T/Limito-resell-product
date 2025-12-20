package com.limito.limitoresellproduct.domain.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.limito.limitoresellproduct.domain.model.Model;

public interface ResellModelRepository {

	Model saveModel(Model model);

	Page<Model> findAllByCategoryIdForAllUser(UUID categoryId, Pageable pageable);

	Model findByIdForAllUserOrElseThrow(UUID modelId);

	Model findByOptionIdOrElseThrow(UUID optionId);
}
