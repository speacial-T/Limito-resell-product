package com.limito.limitoresellproduct.presentation.advice;

import org.springframework.http.HttpStatus;

import com.limito.common.code.ErrorCode;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements ErrorCode {
	INVALID_DOMAIN_INFO(HttpStatus.BAD_REQUEST, "잘못된 도메인 정보입니다."),
	;

	private HttpStatus status;
	private String message;
}
