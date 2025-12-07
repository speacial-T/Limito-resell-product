package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class StockReserveResponseV1 {

	private String errorCode;

	private String message;

	@Setter
	private List<UUID> stockIds;

}
