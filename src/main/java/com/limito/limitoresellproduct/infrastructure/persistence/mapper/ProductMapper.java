package com.limito.limitoresellproduct.infrastructure.persistence.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.limito.limitoresellproduct.domain.model.Model;
import com.limito.limitoresellproduct.domain.model.Option;
import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.domain.vo.Product;
import com.limito.limitoresellproduct.presentation.advice.ProductErrorCode;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductInfosGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockReduceResponseV1;

import jakarta.validation.Valid;

@Component
public class ProductMapper {

	public static Model toEntity(@Valid ProductCreateRequestV1 request) {
		List<Option> options = request.getOptions().stream()
			.map(optionRequest -> Option.create(optionRequest.size()))
			.toList();
		Product product = new Product(
			request.getProductInfo().productName(),
			request.getProductInfo().brandName(),
			request.getProductInfo().categoryId()
		);
		Model model = new Model(
			request.getModelNumber(),
			request.getColor(),
			request.getThumbnailUrl(),
			request.getDetails(),
			product,
			options
		);
		return model;
	}

	public static Stock toEntity(@Valid StockCreateRequestV1 request) {
		Stock stock = new Stock(request.getOptionId(), request.getPrice());
		return stock;
	}

	public static ProductCreateResponseV1 toProductCreateResponseV1(Model model) {
		return ProductCreateResponseV1.builder()
			.productInfo(new ProductCreateResponseV1.ProductInfoResponse(
				model.getProduct().getProductId(),
				model.getProduct().getName(),
				model.getProduct().getBrandName(),
				model.getProduct().getCategoryId()
			))
			.options(model.getOptions().stream()
				.map(o -> new ProductCreateResponseV1.OptionResponseV1(
					o.getOptionId(),
					o.getModelId(),
					o.getSize(),
					o.isInStock(),
					new ProductCreateResponseV1.MinStockResponse(
						o.getMinimumPriceStock().getStockId(),
						o.getMinimumPriceStock().getPrice()
					)
				))
				.toList())
			.build();
	}

	public static StockCreateResponseV1 toStockCreateResponseV1(Stock stock) {
		return StockCreateResponseV1.builder()
			.stockId(stock.getId())
			.optionId(stock.getOptionId())
			.price(stock.getPrice())
			.soldOut(stock.isSoldOut())
			.sellerId(stock.getSellerId())
			.build();
	}

	public static ProductGetResponseV1 toProductGetResponseV1(Model model) {

		List<ProductGetResponseV1.ProductGetResponseOption> mappedOptions = model.getOptions().stream()
			.map(option -> {

				MinimumPriceStock stock = option.getMinimumPriceStock();

				ProductGetResponseV1.ProductGetResponseMinStock minStockRes = null;
				if (stock != null) {
					minStockRes = new ProductGetResponseV1.ProductGetResponseMinStock(
						stock.getStockId(),
						stock.getPrice()
					);
				}

				return new ProductGetResponseV1.ProductGetResponseOption(
					option.getOptionId(),
					option.getSize(),
					minStockRes
				);
			})
			.toList();

		return ProductGetResponseV1.builder()
			.productId(model.getProduct().getProductId())
			.productName(model.getProduct().getName())
			.brandName(model.getProduct().getBrandName())
			.options(mappedOptions)
			.build();
	}

	public static StockReduceResponseV1 toStockReduceResponseV1(ProductErrorCode failReason, List<UUID> stockIds) {
		return StockReduceResponseV1.builder()
			.errorCode(failReason.getMessage()
				.substring(0, 4))
			.message(failReason.getMessage()
				.substring(7))
			.stockIds(stockIds)
			.build();
	}

	// public static StockCancelResponseV1 toStockCancelResponseV1(ProductErrorCode failReason, List<UUID> stockIds) {
	// 	return StockCancelResponseV1.builder()
	// 		.errorCode(failReason.getMessage()
	// 			.substring(0, 4))
	// 		.message(failReason.getMessage()
	// 			.substring(7))
	// 		.stockIds(stockIds)
	// 		.build();
	// }

	public static ProductInfosGetResponseV1 toProductInfosGetResponseV1(Model model, Option option, Stock stock) {
		return ProductInfosGetResponseV1.builder()
			.productId(model.getProduct().getProductId())
			.optionId(option.getOptionId())
			.stockId(stock.getId())
			.productType("RESELL")
			.productName(model.getProduct().getName())
			.brandName(model.getProduct().getBrandName())
			.productColor(model.getColor())
			.productSize(option.getSize())
			.productPrice(stock.getPrice())
			.sellerId(stock.getSellerId())
			.build();
	}
}
