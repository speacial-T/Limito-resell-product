package com.limito.limitoresellproduct.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.limito.common.exception.AppException;
import com.limito.common.security.audit.UserRole;
import com.limito.common.security.auth.PreAuthorized;
import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.domain.vo.Option;
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

	private final ResellProductRepository resellProductRepository;

	@PreAuthorized(UserRole.ADMIN)
	public ProductCreateResponseV1 createProduct(ProductCreateRequestV1 request) {
		Product product = ProductMapper.toEntity(request);
		Product savedProduct = resellProductRepository.saveProduct(product);
		return ProductMapper.toProductCreateResponseV1(savedProduct);
	}

	@Transactional
	public void changeMinimumPriceStock(UUID productId, UUID optionId, MinimumPriceStock minStock) {
		Product product = resellProductRepository.findByIdOrElseThorw(productId);
		product.changeMinimumPriceStock(optionId, minStock);
	}

	public ProductsGetResponseV1 getProducts(UUID categoryId, Pageable pageable) {
		Page<Product> products = resellProductRepository.findAllByCategoryId(categoryId, pageable);
		return ProductMapper.toProductsGetResponseV1(categoryId, products);
	}

	public ProductGetResponseV1 getProduct(UUID resellProductId) {
		Product product = resellProductRepository.findById(resellProductId);
		return ProductMapper.toProductGetResponseV1(product);
	}

	public void validateProductAndOption(UUID productId, UUID optionId) {
		Product product = resellProductRepository.findByIdOrElseThorw(productId);

		Option option = product.getOption(optionId);
		if (option == null) {
			throw AppException.of(ProductErrorCode.WRONG_OPTION_ID);
		}
	}
}
