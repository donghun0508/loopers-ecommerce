package com.loopers.interfaces.api.catalog;


import com.loopers.application.catalog.Result.ProductDetailResult;
import com.loopers.application.catalog.Result.ProductListResult;
import com.loopers.domain.catalog.ProductCriteria.ProductListCriteria.SortType;
import jakarta.validation.constraints.NotNull;

public class ProductV1Dto {

    public record GetListRequest(Long brandId, @NotNull SortType sort) {
    }


    public record GetListResponse(
            Long productId,
            ProductInfo productInfo
    ) {

        public static GetListResponse from(ProductListResult result) {
            return new GetListResponse(
                    result.productId(),
                    new ProductInfo(
                            result.productInfo().productName(),
                            result.productInfo().isSoldOut(),
                            result.productInfo().price(),
                            result.productInfo().brandId(),
                            result.productInfo().brandName(),
                            result.productInfo().likeCount()
                    )
            );
        }

        public record ProductInfo(
                String productName,
                boolean isSoldOut,
                Long price,
                Long brandId,
                String brandName,
                Long likeCount
        ) {

        }
    }

    public record GetDetailResponse(
            Long productId,
            ProductDetailInfo product,
            BrandDetailInfo brand
    ) {

        public record ProductDetailInfo(
                String productName,
                boolean isSoldOut,
                Long price,
                Long likeCount,
                boolean isLiked
        ) {

        }

        public record BrandDetailInfo(
                Long brandId,
                String brandName
        ) {

        }

        public static GetDetailResponse from(ProductDetailResult result) {
            return new GetDetailResponse(
                    result.productId(),
                    new ProductDetailInfo(
                            result.product().productName(),
                            result.product().isSoldOut(),
                            result.product().price(),
                            result.product().likeCount(),
                            result.product().isLiked()
                    ),
                    new BrandDetailInfo(
                            result.brand().brandId(),
                            result.brand().brandName()
                    )
            );
        }
    }


}
