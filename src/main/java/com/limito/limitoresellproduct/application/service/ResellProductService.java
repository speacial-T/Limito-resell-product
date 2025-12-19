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

	private final ResellProductRepository resellProductRepository;

	public ProductCreateResponseV1 createProduct(ProductCreateRequestV1 request) {
		Product product = ProductMapper.toEntity(request);
		Product savedProduct = resellProductRepository.saveProduct(product);
		return ProductMapper.toProductCreateResponseV1(savedProduct);
	}

	@Transactional
	public void changeMinimumPriceStock(UUID productId, UUID optionId, UUID stockId, int price) {
		MinimumPriceStock minimumPriceStock = new MinimumPriceStock(stockId, price);
		Product product = resellProductRepository.findById(productId);
		if (product == null) {
			throw new AppException(ProductErrorCode.WRONG_PRODUCT_ID);
		}
		product.validateActive();
		product.changeMinimumPriceStock(optionId, minimumPriceStock);
	}

	public ProductReadResponseV1 getProducts(UUID categoryId, Pageable pageable) {
		Page<Product> products = resellProductRepository.findAllByCategoryIdForAllUser(categoryId, pageable);
		return ProductReadResponseV1.of(categoryId, products);
	}

	public ProductGetResponseV1 getProduct(UUID resellProductId) {
		Product product = resellProductRepository.findByIdForAllUser(resellProductId);
		if (product == null) {
			throw new AppException(ProductErrorCode.WRONG_PRODUCT_ID);
		}
		return ProductMapper.toProductGetResponseV1(product);
	}

	public void validateProductAndOption(UUID productId, UUID optionId) {
		Product product = resellProductRepository.findById(productId);
		if (product == null) {
			throw new AppException(ProductErrorCode.WRONG_PRODUCT_ID);
		}
		product.validateActive();

		Option option = product.getOption(optionId);
		if (option == null) {
			throw new AppException(ProductErrorCode.WRONG_OPTION_ID);
		}
		option.checkActive();
	}
}
