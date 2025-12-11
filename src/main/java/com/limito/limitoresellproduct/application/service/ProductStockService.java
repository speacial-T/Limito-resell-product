package com.limito.limitoresellproduct.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.repository.ResellProductRepository;
import com.limito.limitoresellproduct.domain.repository.ResellStockRepository;
import com.limito.limitoresellproduct.domain.vo.Option;
import com.limito.limitoresellproduct.presentation.dto.request.ProductInfosGetRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductInfosGetResponseV1;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductStockService {
	private final ResellProductRepository resellProductRepository;
	private final ResellStockRepository resellStockRepository;

	public List<ProductInfosGetResponseV1> getProdutctInfos(List<ProductInfosGetRequestV1> requests) {
		List<ProductInfosGetResponseV1> response = requests.stream()
			.map(request -> {
				Product product = resellProductRepository.findById(request.getProductId());
				Option option = product.getOption(request.getOptionId());
				Stock stock = resellStockRepository.findById(request.getStockId());
				return ProductInfosGetResponseV1.create(product, option, stock);
			})
			.toList();

		return response;
	}
}
