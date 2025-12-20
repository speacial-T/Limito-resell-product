package com.limito.limitoresellproduct.infrastructure.persistence.mapper;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Component;

import com.limito.limitoresellproduct.domain.model.Model;
import com.limito.limitoresellproduct.domain.model.Option;
import com.limito.limitoresellproduct.domain.model.Product;
import com.limito.limitoresellproduct.domain.model.Stock;
import com.limito.limitoresellproduct.domain.vo.MinimumPriceStock;
import com.limito.limitoresellproduct.presentation.dto.request.ProductCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.request.StockCreateRequestV1;
import com.limito.limitoresellproduct.presentation.dto.response.OptionInfosGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductCreateResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductInfosGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.ProductsGetResponseV1;
import com.limito.limitoresellproduct.presentation.dto.response.StockCreateResponseV1;

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
			.modelNumber(model.getModelNumber())
			.color(model.getColor())
			.thumbnailUrl(model.getThumbnailUrl())
			.details(model.getDetails())
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
					option.isInStock(),
					minStockRes
				);
			})
			.toList();

		return ProductGetResponseV1.builder()
			.productId(model.getProduct().getProductId())
			.productName(model.getProduct().getName())
			.brandName(model.getProduct().getBrandName())
			.modelId(model.getModelId())
			.modelNumber(model.getModelNumber())
			.color(model.getColor())
			.thumbnailUrl(model.getThumbnailUrl())
			.details(model.getDetails())
			.options(mappedOptions)
			.build();
	}

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

	public static ProductsGetResponseV1 toProductsGetResponseV1(UUID categoryId, Page<Model> models) {
		List<ProductsGetResponseV1.ProductGetResponse> content =
			models.getContent()
				.stream()
				.flatMap(model ->
					model.getOptions()
						.stream()
						.map(option -> {
							MinimumPriceStock stock = option.getMinimumPriceStock();

							return ProductsGetResponseV1.ProductGetResponse.builder()
								.productId(model.getProduct().getProductId())
								.productName(model.getProduct().getName())
								.brandName(model.getProduct().getBrandName())
								.modelId(model.getModelId())
								.modelNumber(model.getModelNumber())
								.color(model.getColor())
								.thumbnailUrl(model.getThumbnailUrl())
								.details(model.getDetails())
								.build();
						})
				)
				.toList();

		Page<ProductsGetResponseV1.ProductGetResponse> mapped =
			new PageImpl<>(
				content,
				models.getPageable(),
				content.size()
			);

		return ProductsGetResponseV1.builder()
			.categoryId(categoryId)
			.category("임시 카테고리명")
			.products(new PagedModel<>(mapped))
			.build();
	}

	public static OptionInfosGetResponseV1 toOptionInfosGetResponseV1(Model model, Option option) {
		return OptionInfosGetResponseV1.builder()
			.productType("RESELL")
			.productName(model.getProduct().getName())
			.brandName(model.getProduct().getBrandName())
			.productColor(model.getColor())
			.thumbnailUrl(model.getThumbnailUrl())
			.productSize(option.getSize())
			.productPrice(option.getMinimumPriceStock().getPrice())
			.sellerId(option.getMinimumPriceStock().getSellerId())
			.build();
	}
}
