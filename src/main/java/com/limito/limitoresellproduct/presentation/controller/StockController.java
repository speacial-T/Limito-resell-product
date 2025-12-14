package com.limito.limitoresellproduct.presentation.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.limito.limitoresellproduct.application.service.ResellStockService;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/resell-product")
@RequiredArgsConstructor
public class StockController {

	private final ResellStockService stockService;

	@PostMapping("/stock")
	public ResponseEntity<StockCreateResponseV1> createProduct(@Valid @RequestBody StockCreateRequestV1 request) {
		StockCreateResponseV1 response = stockService.createStock(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}
}
