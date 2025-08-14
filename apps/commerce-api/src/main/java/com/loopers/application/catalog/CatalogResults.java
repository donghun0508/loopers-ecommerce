package com.loopers.application.catalog;

import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.Product;
import lombok.Builder;

public class CatalogResults {

    public record BrandDetailResult(Long brandId, String brandName, Long totalProductCount) {

    }

    @Builder
    public record ProductDetailResult(Long productId, ProductDetailInfo product, BrandDetailInfo brand) {

        @Builder
        public record ProductDetailInfo(
            String productName,
            boolean isSoldOut,
            Long price,
            Long likeCount,
            boolean isLiked
        ) {

            public static ProductDetailInfo from(Product product, boolean isLiked) {
                return new ProductDetailInfo(
                    product.getName(),
                    product.isSoldOut(),
                    product.getUnitPrice().value(),
                    product.getHeartCount(),
                    isLiked
                );
            }
        }

        @Builder
        public record BrandDetailInfo(Long brandId, String brandName) {

            public static BrandDetailInfo from(Brand brand) {
                return new BrandDetailInfo(
                    brand.getId(),
                    brand.getName()
                );
            }
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
                return new ProductListResult(
                    product.getId()
                    , new ProductInfo(
                    product.getName(),
                    product.isSoldOut(),
                    product.getUnitPrice().value(),
                    brand.getId(),
                    brand.getName(),
                    product.getHeartCount())
                );
            }
        }
    }
}
