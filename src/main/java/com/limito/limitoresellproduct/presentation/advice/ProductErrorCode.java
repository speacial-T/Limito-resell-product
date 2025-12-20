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
	WRONG_MODEL_ID(HttpStatus.BAD_REQUEST, "잘못된 리셀 모델 ID입니다."),
	WRONG_OPTION_ID(HttpStatus.BAD_REQUEST, "잘못된 리셀 상품 옵션 ID입니다."),
	WRONG_STOCK_ID(HttpStatus.BAD_REQUEST, "잘못된 리셀 상품 재고 ID입니다."),
	OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "재고 부족"),
	INACTIVE_PRODUCT(HttpStatus.BAD_REQUEST, "비활성화된 상품입니다."),
	INACTIVE_OPTION(HttpStatus.BAD_REQUEST, "비활성화된 옵션입니다."),
	INACTIVE_STOCK(HttpStatus.BAD_REQUEST, "비활성화된 재고입니다."),
	INACTIVE_MODEL(HttpStatus.BAD_REQUEST, "비활성화된 모델입니다."),
	ALREADY_EXIST(HttpStatus.BAD_REQUEST, "이미 재고가 존재합니다.");

	private HttpStatus status;
	private String message;
}
