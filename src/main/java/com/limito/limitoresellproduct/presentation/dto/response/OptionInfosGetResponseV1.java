package com.limito.limitoresellproduct.presentation.dto.response;

import lombok.Builder;

@Builder
public class OptionInfosGetResponseV1 {
	private String productType;
	private String productName;
	private String productColor;
	private String productSize;
	private int productPrice;
	private String brandName;
	private String thumbnailUrl;
	private Long sellerId;
}
