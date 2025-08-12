package com.loopers.domain.catalog;

import com.loopers.domain.user.AccountId;
import org.springframework.data.domain.Pageable;

public class ProductCriteria {

    public record ProductListCriteria(Long brandId, SortType sort, Pageable pageable) {

        public enum SortType {
            LATEST,
            PRICE_ASC,
            LIKES_DESC
        }
    }

    public record ProductDetailCriteria(Long productId, AccountId accountId) {

        public ProductDetailCriteria(Long productId, String accountId) {
            this(productId, accountId != null && !accountId.isBlank() ? AccountId.of(accountId) : null);
        }
    }
}
