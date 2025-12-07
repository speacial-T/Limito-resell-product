package com.limito.limitoresellproduct.application.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductReadResponseV1;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResellProductService {

	private final ResellProductRepository resellProductRepository;

	public ProductCreateResponseV1 createProduct(@Valid ProductCreateRequestV1 request) {
		Product product = ProductMapper.toEntity(request);
		Product savedProduct = resellProductRepository.saveProduct(product);
		return ProductMapper.toDto(savedProduct);
	}

	@Transactional
	public void changeMinimumPriceStock(UUID productId, UUID optionId, UUID stockId, int price) {
		MinimumPriceStock minimumPriceStock = new MinimumPriceStock(stockId, price);
		Product product = resellProductRepository.findById(productId);
		product.changeMinimumPriceStock(optionId, minimumPriceStock);
	}

	public ProductReadResponseV1 getProducts(UUID categoryId, Pageable pageable) {
		Page<Product> products = resellProductRepository.findAllByCategoryId(categoryId, pageable);
		return ProductReadResponseV1.of(categoryId, products);
	}

	public ProductGetResponseV1 getProduct(@NotNull(message = "") UUID resellProductId) {
		Product product = resellProductRepository.findById(resellProductId);
		return ProductGetResponseV1.of(product);
	}
}
