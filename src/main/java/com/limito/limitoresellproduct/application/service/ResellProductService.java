package com.limito.limitoresellproduct.application.service;

import org.springframework.stereotype.Service;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResellProductService {

	private final ResellProductRepository resellProductRepository;

	public ProductCreateResponseV1 createProduct(@Valid ProductCreateRequestV1 request) {
		Product product = new Product(
			request.getProductName(),
			request.getBrandName(),
			request.getCategoryId(),
			request.getOptions()
		);
		Product savedProduct = resellProductRepository.save(product);
		return savedProduct.toDto();
	}
}
