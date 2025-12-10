package com.limito.limitoresellproduct.presentation.controller;

import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.limito.common.audit.UserContextHolder;
import com.limito.common.code.CommonErrorCode;
import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.application.service.ResellProductService;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductReadResponseV1;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/resell-product")
@RequiredArgsConstructor
public class ResellProductController {

	private final ResellProductService resellProductService;

	@PostMapping
	public ResponseEntity<ProductCreateResponseV1> createProduct(@Valid @RequestBody ProductCreateRequestV1 request) {
		checkRole("ADMIN");
		ProductCreateResponseV1 response = resellProductService.createProduct(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping
	public ResponseEntity<ProductReadResponseV1> getProducts(
		@RequestParam @NotNull(message = "상품 목록 조회 시 카테고리ID는 필수 입력값입니다.") UUID categoryId,
		@PageableDefault Pageable pageable
	) {
		ProductReadResponseV1 response = resellProductService.getProducts(categoryId, pageable);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/{resellProductId}")
	public ResponseEntity<ProductGetResponseV1> getProduct(
		@PathVariable @NotNull(message = "") UUID resellProductId
	) {
		ProductGetResponseV1 response = resellProductService.getProduct(resellProductId);
		return ResponseEntity.ok().body(response);
	}

	private void checkRole(String expectedRole) {
		String role = UserContextHolder.get().getRole();
		if (!role.equals(expectedRole)) {
			throw new AppException(CommonErrorCode.FORBIDDEN);
		}
	}
}
