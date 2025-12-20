package com.limito.limitoresellproduct.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.domain.vo.Option;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductReadResponseV1;

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

	@Transactional
	public void changeMinimumPriceStock(UUID productId, UUID optionId, MinimumPriceStock minStock) {
		Model model = resellModelRepository.findByIdOrElseThorw(productId);
		product.validateActive();
		product.changeMinimumPriceStock(optionId, minStock);
	}

	public ProductsGetResponseV1 getProducts(UUID categoryId, Pageable pageable) {
		Page<Model> models = resellProductRepository.findAllByCategoryIdForAllUser(categoryId, pageable);
		return ProductMapper.toProductsGetResponseV1(categoryId, models);
	}

	public ProductGetResponseV1 getProduct(UUID resellProductId) {
		Model model = resellModelRepository.findByIdForAllUserOrElseThrow(resellProductId);
		return ProductMapper.toProductGetResponseV1(product);
	}

	public void validateProductAndOption(UUID productId, UUID optionId) {
		Model model = resellProductRepository.findByIdOrElseThorw(productId);
		model.validateActive();

		Option option = model.getOption(optionId);
		if (option == null) {
			throw AppException.of(ProductErrorCode.WRONG_OPTION_ID);
		}
		option.checkActive();
	}
}
