package com.limito.limitoresellproduct.infrastructure.persistence.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.domain.vo.Option;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCancelResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockReduceResponseV1;

import jakarta.validation.Valid;

@Component
public class ProductMapper {

	public static Product toEntity(@Valid ProductCreateRequestV1 request) {
		List<Option> options = request.getOptions().stream()
			.map(optionRequest -> new Option(
				null,
				optionRequest.modelNumber(),
				optionRequest.size(),
				optionRequest.color(),
				optionRequest.thumbnailUrl(),
				optionRequest.details(),
				null)
			)
			.toList();
		Product product = new Product(
			request.getProductName(),
			request.getBrandName(),
			request.getCategoryId(),
			options
		);
		return product;
	}

	public static Stock toEntity(@Valid StockCreateRequestV1 request) {
		Stock stock = new Stock(request.getOptionId(), request.getPrice());
		return stock;
	}

	public static ProductCreateResponseV1 toProductCreateResponseV1(Product product) {
		return ProductCreateResponseV1.builder()
			.productId(product.getProductId())
			.productName(product.getName())
			.brandName(product.getBrandName())
			.categoryId(product.getCategoryId())
			.options(product.getOptions().stream()
				.map(o -> new ProductCreateResponseV1.OptionResponseV1(
					o.getOptionId(),
					o.getModelNumber(),
					o.getSize(),
					o.getColor(),
					o.getThumbnailUrl(),
					o.getDetails(),
					new ProductCreateResponseV1.MinStockResponse(
						o.getMinimumPriceStock().getMinimumPriceStockId(),
						o.getMinimumPriceStock().getMinimumPriceStockPrice()
					)
				))
				.toList())
			.build();
	}

	public static StockCreateResponseV1 toDto(Stock stock) {
		return StockCreateResponseV1.builder()
			.stockId(stock.getId())
			.optionId(stock.getOptionId())
			.price(stock.getPrice())
			.isDeleted(stock.isDeleted())
			.sellerId(stock.getSellerId())
			.build();
	}

	public static ProductGetResponseV1 toProductGetResponseV1(Product product) {

		List<ProductGetResponseV1.ProductGetResponseOption> mappedOptions = product.getOptions().stream()
			.map(option -> {

				MinimumPriceStock stock = option.getMinimumPriceStock();

				ProductGetResponseV1.ProductGetResponseMinStock minStockRes = null;
				if (stock != null) {
					minStockRes = new ProductGetResponseV1.ProductGetResponseMinStock(
						stock.getMinimumPriceStockId(),
						stock.getMinimumPriceStockPrice()
					);
				}

				return new ProductGetResponseV1.ProductGetResponseOption(
					option.getOptionId(),
					option.getModelNumber(),
					option.getSize(),
					option.getColor(),
					option.getThumbnailUrl(),
					option.getDetails(),
					minStockRes
				);
			})
			.toList();

		return ProductGetResponseV1.builder()
			.productId(product.getProductId())
			.productName(product.getName())
			.brandName(product.getBrandName())
			.options(mappedOptions)
			.build();
	}

	public static StockReduceResponseV1 toStockReduceResponseV1(ProductErrorCode failReason, List<UUID> stockIds) {
		return StockReduceResponseV1.builder()
			.errorCode(failReason.getMessage().substring(0, 4))
			.message(failReason.getMessage().substring(7))
			.stockIds(stockIds)
			.build();
	}

	public static StockCancelResponseV1 toStockCancelResponseV1(ProductErrorCode failReason, List<UUID> stockIds) {
		return StockCancelResponseV1.builder()
			.errorCode(failReason.getMessage().substring(0, 4))
			.message(failReason.getMessage().substring(7))
			.stockIds(stockIds)
			.build();
	}
}
