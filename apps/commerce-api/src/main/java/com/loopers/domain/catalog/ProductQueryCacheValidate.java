package com.loopers.domain.catalog;

import org.springframework.stereotype.Component;

@Component
public class ProductQueryCacheValidate {

    public boolean useCache(int pageNumber) {
        return pageNumber <= 2;
    }

    public boolean useCache(Long productId) {
        return productId <= 10L;
    }
}
