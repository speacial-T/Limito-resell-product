package com.limito.limitoresellproduct.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.limito.common.audit.UserContextHolder;
import com.limito.common.code.CommonErrorCode;
import com.limito.common.exception.AppException;
import com.limito.limitoresellproduct.application.service.ResellStockService;
import com.limito.limitoresellproduct.presentation.dto.request.StockReduceRequest;
import com.limito.limitoresellproduct.presentation.dto.request.StockRollbackRequest;
import com.limito.limitoresellproduct.presentation.dto.response.InternalResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/v1/resell-products/stock")
@RequiredArgsConstructor
public class ResellProductInternalController {

	private final ResellStockService resellStockService;

	@PostMapping("/reserve")
	public ResponseEntity<Void> reserveStock(@RequestBody List<UUID> stockIds) {
		resellStockService.reserveStocks(stockIds);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/reduce")
	public ResponseEntity<Void> reduceStock(@Valid @RequestBody List<StockReduceRequest> request) {
		resellStockService.reduceStocks(request);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/cancel")
	public ResponseEntity<InternalResponse> cancelStock(@RequestBody List<UUID> stockIds) {
		resellStockService.cancelStocks(stockIds);
		return ResponseEntity.ok().build();
	}

	@PostMapping("/rollback")
	public ResponseEntity<Object> rollbackStock(@Valid @RequestBody List<StockRollbackRequest> request) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	private void checkRole(String expectedRole) {
		String role = UserContextHolder.get().getRole();
		if (!role.equals(expectedRole)) {
			throw new AppException(CommonErrorCode.FORBIDDEN);
		}
	}
}
