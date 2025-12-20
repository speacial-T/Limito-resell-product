package com.limito.limitoresellproduct.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.limito.limitoresellproduct.application.service.ProductStockService;
import com.limito.limitoresellproduct.application.service.ResellStockService;
import com.limito.limitoresellproduct.presentation.dto.request.StockReduceRequest;
import com.limito.limitoresellproduct.presentation.dto.request.StockRollbackRequest;
import com.limito.limitoresellproduct.presentation.dto.response.OptionInfosGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductInfosGetResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/v1/resell-products")
@RequiredArgsConstructor
public class ResellProductInternalController {

	private final ResellStockService resellStockService;
	private final ProductStockService productStockService;

	@PostMapping("/stock/reserve")
	public ResponseEntity<Void> reserveStock(@RequestBody List<UUID> stockIds) {
		resellStockService.reserveStocks(stockIds);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/stock/reduce")
	public ResponseEntity<Void> reduceStock(@Valid @RequestBody List<StockReduceRequest> request) {
		resellStockService.reduceStocks(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/stock/cancel")
	public ResponseEntity<Void> cancelStock(@RequestBody List<UUID> stockIds) {
		resellStockService.cancelStocks(stockIds);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/stock/rollback")
	public ResponseEntity<Void> rollbackStock(@Valid @RequestBody List<StockRollbackRequest> request) {
		resellStockService.rollbackStocks(request);
		return ResponseEntity.ok().build();
	}

	@GetMapping("/productInfo")
	public ResponseEntity<List<ProductInfosGetResponseV1>> getProductInfos(
		@Valid @RequestParam List<UUID> stockIds
	) {
		List<ProductInfosGetResponseV1> response = productStockService.getProdutctInfos(stockIds);
		return ResponseEntity.ok().body(response);
	}

	@GetMapping("/optionInfo")
	public ResponseEntity<List<OptionInfosGetResponseV1>> getOptionInfos(
		@Valid @RequestParam List<UUID> optionIds
	) {
		List<OptionInfosGetResponseV1> response = productStockService.getOptiontInfos(optionIds);
		return ResponseEntity.ok().body(response);
	}
}
