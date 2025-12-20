package com.limito.limitoresellproduct.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.limito.limitoresellproduct.domain.model.Model;
import com.limito.limitoresellproduct.domain.model.Option;
import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.repository.ResellModelRepository;
import com.limito.limitoresellproduct.domain.repository.ResellStockRepository;
import com.limito.limitoresellproduct.infrastructure.persistence.mapper.ProductMapper;
import com.limito.limitoresellproduct.presentation.dto.response.OptionInfosGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductInfosGetResponseV1;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductStockService {
	private final ResellModelRepository resellModelRepository;
	private final ResellStockRepository resellStockRepository;

	public List<ProductInfosGetResponseV1> getProdutctInfos(List<UUID> stockIds) {
		List<ProductInfosGetResponseV1> response = stockIds.stream()
			.map(stockId -> {
				Stock stock = resellStockRepository.findById(stockId);
				Model model = resellModelRepository.findByOptionIdOrElseThrow(stock.getOptionId());
				Option option = model.getOption(stock.getOptionId());
				return ProductMapper.toProductInfosGetResponseV1(model, option, stock);
			})
			.toList();

		return response;
	}

	public List<OptionInfosGetResponseV1> getOptiontInfos(@Valid List<UUID> optionIds) {
		List<OptionInfosGetResponseV1> response = optionIds.stream()
			.map(optionId -> {
				Model model = resellModelRepository.findByOptionIdOrElseThrow(optionId);
				Option option = model.getOption(optionId);
				return ProductMapper.toOptionInfosGetResponseV1(model, option);
			})
			.toList();

		return response;
	}
}
