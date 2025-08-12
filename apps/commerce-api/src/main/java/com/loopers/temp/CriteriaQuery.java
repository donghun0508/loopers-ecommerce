//package com.loopers.temp;
//
//import com.loopers.domain.user.AccountId;
//import org.springframework.data.domain.Pageable;
//import org.springframework.util.StringUtils;
//
//import java.util.Arrays;
//
//public class CriteriaQuery {
//
//    public record GetBrandDetailCriteria(Long brandId) {
//
//        public static GetBrandDetailCriteria of(Long brandId) {
//            return new GetBrandDetailCriteria(brandId);
//        }
//    }
//
//    public record GetProductListCriteria(Long brandId, SortType sort, Pageable pageable) {
//
//        public enum SortType {
//            LATEST,
//            PRICE_ASC,
//            LIKES_DESC
//        }
//
//        public GetProductListCriteria(Long brandId, String sort, Pageable pageable) {
//            this(
//                    brandId,
//                    Arrays.stream(SortType.values())
//                            .filter(type -> type.name().equalsIgnoreCase(sort))
//                            .findFirst()
//                            .orElse(SortType.LATEST),
//                    pageable);
//        }
//
//        public static GetProductListCriteria of(Long brandId, String sort, Pageable pageable) {
//            return new GetProductListCriteria(brandId, sort, pageable);
//        }
//    }
//
//    public record GetProductDetailCriteria(Long productId, AccountId accountId) {
//
//        public GetProductDetailCriteria(Long productId, String accountId) {
//            this(productId, StringUtils.hasText(accountId) ? AccountId.of(accountId) : null);
//        }
//
//        public static GetProductDetailCriteria of(Long productId, String accountId) {
//            return new GetProductDetailCriteria(productId, accountId);
//        }
//    }
//}
