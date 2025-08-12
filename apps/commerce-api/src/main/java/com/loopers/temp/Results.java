//package com.loopers.temp;
//
//public class Results {
//    public record GetBrandDetailResult(Long brandId, String brandName, Long totalProductCount) {
//
//    }
//
//    public record GetProductListResult(Long productId, ProductInfo productInfo) {
//
//        public record ProductInfo(
//                String productName,
//                boolean isSoldOut,
//                Long price,
//                Long brandId,
//                String brandName,
//                Long likeCount
//        ) {
//
//        }
//    }
//
//
//    public record GetProductDetailResult(Long productId, ProductDetailInfo product, BrandDetailInfo brand) {
//
//        public record ProductDetailInfo(
//                String productName,
//                boolean isSoldOut,
//                Long price,
//                Long likeCount,
//                boolean isLiked
//        ) {
//
//        }
//
//        public record BrandDetailInfo(Long brandId, String brandName) {
//
//        }
//    }
//}
