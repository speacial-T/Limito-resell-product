package com.limito.limitoresellproduct.infrastructure.persistence.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.domain.model.Model;
import com.limito.limitoresellproduct.domain.model.Option;
import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellModelRepository;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ResellModelRepositoryImpl implements ResellModelRepository {

	private final ResellModelJpaRepository modelJpaRepository;
	private final ResellProductJpaRepository productJpaRepository;
	private final ResellOptionJpaRepository optionJpaRepository;

	@Override
	public Model saveModel(Model model) {
		Product product = model.getProduct();
		if (product.getProductId() == null) {
			product = productJpaRepository.save(model.getProduct());
		}
		model.giveProduct(product);
		Model savedModel = modelJpaRepository.save(model);
		savedModel.giveModelIdToOptions();
		modelJpaRepository.flush();
		return savedModel;
	}

	@Override
	public Model findById(UUID modelId) {
		return modelJpaRepository.findById(modelId).orElse(null);
	}

	@Override
	public Model findByIdForAllUser(UUID modelId) {
		return modelJpaRepository.findByModelIdAndDeletedAtIsNull(modelId).orElse(null);
	}

	@Override
	public Page<Model> findAllByCategoryIdForAllUser(UUID categoryId, Pageable pageable) {
		return modelJpaRepository.findAllByCategoryIdForAllUser(categoryId, pageable);
	}

	@Override
	public Model findByIdForAllUserOrElseThrow(UUID modelId) {
		return modelJpaRepository.findByModelIdAndDeletedAtIsNull(modelId).orElseThrow(() ->
			AppException.of(ProductErrorCode.WRONG_MODEL_ID));
	}

	@Override
	public Model findByOptionIdOrElseThrow(UUID optionId) {
		Option option = optionJpaRepository.findById(optionId).orElse(null);
		if (option == null) {
			throw AppException.of(ProductErrorCode.WRONG_OPTION_ID);
		}
		return modelJpaRepository.findById(option.getModelId()).orElseThrow(() ->
			AppException.of(ProductErrorCode.WRONG_MODEL_ID));
	}
}
