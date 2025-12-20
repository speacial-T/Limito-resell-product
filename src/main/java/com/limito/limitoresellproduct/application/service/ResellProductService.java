package com.limito.limitoresellproduct.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.limito.common.exception.AppException;
import com.limito.common.security.audit.UserRole;
import com.limito.common.security.auth.PreAuthorized;
import com.limito.limitoresellproduct.domain.model.Model;
import com.limito.limitoresellproduct.domain.model.Option;
import com.limito.limitoresellproduct.domain.repository.ResellModelRepository;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductsGetResponseV1;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResellProductService {

	private final ResellModelRepository resellModelRepository;

	@PreAuthorized(UserRole.ADMIN)
	public ProductCreateResponseV1 createProduct(ProductCreateRequestV1 request) {
		Model model = ProductMapper.toEntity(request);
		Model savedModel = resellModelRepository.saveModel(model);
		return ProductMapper.toProductCreateResponseV1(savedModel);
	}

	public ProductsGetResponseV1 getModels(UUID categoryId, Pageable pageable) {
		Page<Model> models = resellModelRepository.findAllByCategoryIdForAllUser(categoryId, pageable);
		return ProductMapper.toProductsGetResponseV1(categoryId, models);
	}

	public ProductGetResponseV1 getModel(UUID modelId) {
		Model model = resellModelRepository.findByIdForAllUserOrElseThrow(modelId);
		return ProductMapper.toProductGetResponseV1(model);
	}

	@Transactional
	public void changeMinimumPriceStock(UUID modelId, UUID optionId, MinimumPriceStock minStock) {
		Model model = resellModelRepository.findByOptionIdOrElseThrow(optionId);
		// Model model = resellModelRepository.findByIdForAllUserOrElseThrow(modelId);
		model.validateActive();
		model.changeMinimumPriceStock(optionId, minStock);
	}

	public void validateModelAndOption(UUID modelId, UUID optionId) {
		Model model = resellModelRepository.findByIdForAllUserOrElseThrow(modelId);
		model.validateActive();

		Option option = model.getOption(optionId);
		if (option == null) {
			throw AppException.of(ProductErrorCode.WRONG_OPTION_ID);
		}
		option.checkActive();
	}
}
