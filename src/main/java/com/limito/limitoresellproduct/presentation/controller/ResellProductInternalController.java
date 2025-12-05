package com.limito.limitoresellproduct.presentation.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.limito.limitoresellproduct.presentation.dto.request.StockReduceRequest;
import com.limito.limitoresellproduct.presentation.dto.request.StockRollbackRequest;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/internal/v1/resell-products")
public class ResellProductInternalController {

	@PostMapping("/stock/reserve")
	public ResponseEntity<Object> reserveStock(@RequestBody List<UUID> stockIds) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/stock/reduce")
	public ResponseEntity<Object> reduceStock(@Valid @RequestBody List<StockReduceRequest> request) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/stock/cancel")
	public ResponseEntity<Object> cancelStock(@RequestBody List<UUID> stockIds) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/stock/rollback")
	public ResponseEntity<Object> rollbackStock(@Valid @RequestBody List<StockRollbackRequest> request) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
