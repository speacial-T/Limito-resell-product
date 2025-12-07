package com.limito.limitoresellproduct.application.service;

import org.springframework.stereotype.Service;

import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.repository.ResellStockRepository;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ResellStockService {

	private final ResellStockRepository resellStockRepository;

	public StockCreateResponseV1 createStock(@Valid StockCreateRequestV1 request) {
		Stock stock = ProductMapper.toEntity(request);

		// TODO optionID 존재 확인

		Stock savedStock = resellStockRepository.saveStock(stock);
		return ProductMapper.toDto(savedStock);
	}
}
