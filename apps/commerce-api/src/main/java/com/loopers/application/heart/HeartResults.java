package com.loopers.application.heart;

public class HeartResults {
    public record HeartResult(
            ProductInfo product
    ) {

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
}
