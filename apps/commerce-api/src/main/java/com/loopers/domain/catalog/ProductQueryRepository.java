package com.loopers.domain.catalog;

import org.springframework.data.domain.Page;

public interface ProductQueryRepository {
    Page<Product> findByProductListCriteria(ProductCriteria.ProductListCriteria criteria);
}
