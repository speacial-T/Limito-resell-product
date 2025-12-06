package com.limito.limitoresellproduct.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;
import com.limito.limitoresellproduct.domain.vo.Option;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResellProductService {

	private final ResellProductRepository resellProductRepository;

	public ProductCreateResponseV1 createProduct(@Valid ProductCreateRequestV1 request) {
		List<Option> options = request.getOptions().stream()
			.map(optionRequest -> new Option(
				null,
				optionRequest.modelNumber(),
				optionRequest.size(),
				optionRequest.color(),
				optionRequest.thumbnailUrl(),
				optionRequest.details(),
				null)
			)
			.toList();
		Product product = new Product(
			request.getProductName(),
			request.getBrandName(),
			request.getCategoryId(),
			options
		);
		Product savedProduct = resellProductRepository.saveProduct(product);
		return ProductMapper.toDto(savedProduct);
	}
}
