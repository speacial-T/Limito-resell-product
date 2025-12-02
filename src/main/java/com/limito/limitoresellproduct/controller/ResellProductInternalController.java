package com.limito.limitoresellproduct.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/internal/v1/resell-products")
public class ResellProductInternalController {

	@PostMapping("/stock/reserve")
	public ResponseEntity<Object> reserveStock(List<UUID> stockIds) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/stock/reduce")
	public ResponseEntity<Object> reduceStock(List<UUID> stockIds) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/stock/cancel")
	public ResponseEntity<Object> cancelStock(List<UUID> stockIds) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	@PostMapping("/stock/rollback")
	public ResponseEntity<Object> rollbackStock(List<UUID> stockIds) {
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}
