package com.limito.limitoresellproduct.presentation.advice;

import org.springframework.http.HttpStatus;

import com.limito.common.code.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements ErrorCode {
	INVALID_DOMAIN_INFO(HttpStatus.BAD_REQUEST, "잘못된 도메인 정보입니다."),
	WRONG_PRODUCT_ID(HttpStatus.BAD_REQUEST, "잘못된 리셀 상품 ID입니다."),
	WRONG_OPTION_ID(HttpStatus.BAD_REQUEST, "잘못된 리셀 상품 옵션 ID입니다."),
	WRONG_STOCK_ID(HttpStatus.BAD_REQUEST, "잘못된 리셀 상품 재고 ID입니다."),
	OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고 부족");

	private HttpStatus status;
	private String message;
}
