package com.limito.limitoresellproduct.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.domain.model.Model;
import com.limito.limitoresellproduct.domain.model.Option;
import com.limito.limitoresellproduct.domain.repository.ResellModelRepository;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
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

	public ProductCreateResponseV1 createProduct(ProductCreateRequestV1 request) {
		Model model = ProductMapper.toEntity(request);
		Model savedModel = resellModelRepository.saveModel(model);
		return ProductMapper.toProductCreateResponseV1(savedModel);
	}

	@Transactional
	public void changeMinimumPriceStock(UUID productId, UUID optionId, UUID stockId, int price) {
		MinimumPriceStock minimumPriceStock = new MinimumPriceStock(stockId, price);
		Model model = resellModelRepository.findById(productId);
		if (model == null) {
			throw new AppException(ProductErrorCode.WRONG_PRODUCT_ID);
		}
		model.validateActive();
		model.changeMinimumPriceStock(optionId, minimumPriceStock);
	}

	public ProductReadResponseV1 getProducts(UUID categoryId, Pageable pageable) {
		Page<Model> models = resellModelRepository.findAllByCategoryIdForAllUser(categoryId, pageable);
		return ProductReadResponseV1.of(categoryId, models);
	}

	public ProductGetResponseV1 getProduct(UUID resellProductId) {
		Model model = resellModelRepository.findByIdForAllUser(resellProductId);
		if (model == null) {
			throw new AppException(ProductErrorCode.WRONG_PRODUCT_ID);
		}
		return ProductMapper.toProductGetResponseV1(model);
	}

	public void validateProductAndOption(UUID productId, UUID optionId) {
		Model model = resellModelRepository.findById(productId);
		if (model == null) {
			throw new AppException(ProductErrorCode.WRONG_PRODUCT_ID);
		}
		model.validateActive();

		Option option = model.getOption(optionId);
		if (option == null) {
			throw new AppException(ProductErrorCode.WRONG_OPTION_ID);
		}
		option.checkActive();
	}
}
