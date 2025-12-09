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
import com.limito.limitoresellproduct.presentation.dto.response.StockReduceResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockReserveResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal/v1/resell-products/stock")
@RequiredArgsConstructor
public class ResellProductInternalController {

	private final ResellStockService resellStockService;

	@PostMapping("/reserve")
	public ResponseEntity<InternalResponse> reserveStock(@RequestBody List<UUID> stockIds) {
		// checkRole("USER");
		StockReserveResponseV1 response = resellStockService.reserveStocks(stockIds);
		return makeResponseWithHttpStatus(response);
	}

	@PostMapping("/reduce")
	public ResponseEntity<InternalResponse> reduceStock(@Valid @RequestBody List<StockReduceRequest> request) {
		// checkRole("USER");
		StockReduceResponseV1 response = resellStockService.reduceStocks(request);
		return makeResponseWithHttpStatus(response);
	}

	@PostMapping("/cancel")
	public ResponseEntity<Object> cancelStock(@RequestBody List<UUID> stockIds) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
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

	private ResponseEntity<InternalResponse> makeResponseWithHttpStatus(InternalResponse response) {
		if (response == null) {
			return ResponseEntity.status(HttpStatus.OK).body(null);
		}
		if (response.getErrorCode().equals("E001")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		if (response.getErrorCode().equals("E002")) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
		}
		return null;
	}
}
