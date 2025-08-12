package com.loopers.application.catalog;

import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.Product;

public class Result {
    public record BrandDetailResult(Long brandId, String brandName, Long totalProductCount) {

    }

    public record ProductDetailResult(Long productId, ProductDetailResult.ProductDetailInfo product,
                                      ProductDetailResult.BrandDetailInfo brand) {
        public record ProductDetailInfo(
                String productName,
                boolean isSoldOut,
                Long price,
                Long likeCount,
                boolean isLiked
        ) {

        }

        public record BrandDetailInfo(Long brandId, String brandName) {

        }
    }

    public record ProductListResult(Long productId, ProductListResult.ProductInfo productInfo) {
        public record ProductInfo(
                String productName,
                boolean isSoldOut,
                Long price,
                Long brandId,
                String brandName,
                Long likeCount
        ) {
            public static ProductListResult of(Product product, Brand brand) {
                return null;
            }
        }
    }
}
