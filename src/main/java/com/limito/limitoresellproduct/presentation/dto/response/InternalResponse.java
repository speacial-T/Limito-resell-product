package com.limito.limitoresellproduct.presentation.dto.response;

import java.util.List;
import java.util.UUID;

public interface InternalResponse {

	public String getErrorCode();

	public String getMessage();

	public List<UUID> getStockIds();
}
