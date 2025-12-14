package com.limito.limitoresellproduct.infrastructure.persistence.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Component;

import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.domain.vo.Option;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductInfosGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductsGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;

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

	public static ProductGetResponseV1 toProductGetResponseV1(Product product) {

		List<ProductGetResponseV1.ProductGetResponseOption> mappedOptions = product.getOptions().stream()
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
					option.getModelNumber(),
					option.getSize(),
					option.getColor(),
					option.getThumbnailUrl(),
					option.getDetails(),
					option.isInStock(),
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

	public static ProductInfosGetResponseV1 toProductInfosGetResponseV1(Product product, Option option, Stock stock) {
		return ProductInfosGetResponseV1.builder()
			.productId(product.getProductId())
			.optionId(option.getOptionId())
			.stockId(stock.getId())
			.productType("RESELL")
			.productName(product.getName())
			.brandName(product.getBrandName())
			.productColor(option.getColor())
			.productSize(option.getSize())
			.productPrice(stock.getPrice())
			.sellerId(stock.getSellerId())
			.build();
	}

	public static ProductsGetResponseV1 toProductsGetResponseV1(UUID categoryId, Page<Product> products) {
		List<ProductsGetResponseV1.ProductGetResponse> content =
			products.getContent().stream()
				.flatMap(product ->
					product.getOptions().stream()
						.map(option -> {
							MinimumPriceStock stock = option.getMinimumPriceStock();

							return new ProductsGetResponseV1.ProductGetResponse(
								product.getProductId(),
								product.getName(),
								product.getBrandName(),
								option.getOptionId(),
								option.getModelNumber(),
								option.getColor(),
								option.getSize(),
								option.getThumbnailUrl(),
								option.getDetails(),
								option.isInStock(),
								stock.getStockId(),
								stock.getPrice()
							);
						})
				)
				.toList();

		Page<ProductsGetResponseV1.ProductGetResponse> mapped =
			new PageImpl<>(
				content,
				products.getPageable(),
				content.size()
			);

		return ProductsGetResponseV1.builder()
			.categoryId(categoryId)
			.category("임시 카테고리명")
			.products(new PagedModel<>(mapped))
			.build();
	}
}
