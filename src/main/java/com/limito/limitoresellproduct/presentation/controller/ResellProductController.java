package com.limito.limitoresellproduct.presentation.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.limito.limitoresellproduct.application.service.ResellProductService;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/resell-product")
@RequiredArgsConstructor
public class ResellProductController {

	private final ResellProductService resellProductService;

	@PostMapping
	public ResponseEntity<ProductCreateResponseV1> createProduct(@Valid @RequestBody ProductCreateRequestV1 request) {
		ProductCreateResponseV1 response = resellProductService.createProduct(request);
		return ResponseEntity.ok().body(response);
	}
}
