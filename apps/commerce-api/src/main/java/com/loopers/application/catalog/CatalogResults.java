package com.loopers.application.catalog;

import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.entity.Brand;
import com.loopers.domain.catalog.entity.Product;
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

            public static ProductDetailInfo from(ProductRead product, boolean isLiked) {
                return new ProductDetailInfo(
                    product.getName(),
                    product.isSoldOut(),
                    product.getUnitPrice(),
                    product.getHeartCount(),
                    isLiked
                );
            }
        }

        @Builder
        public record BrandDetailInfo(Long brandId, String brandName) {

            public static BrandDetailInfo from(ProductRead productRead) {
                return new BrandDetailInfo(
                    productRead.getBrandId(),
                    productRead.getBrandName()
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

            public static ProductListResult of(ProductRead product) {
                return new ProductListResult(
                    product.getId()
                    , new ProductInfo(
                    product.getName(),
                    product.isSoldOut(),
                    product.getUnitPrice(),
                    product.getBrandId(),
                    product.getBrandName(),
                    product.getHeartCount())
                );
            }
        }
    }
}
