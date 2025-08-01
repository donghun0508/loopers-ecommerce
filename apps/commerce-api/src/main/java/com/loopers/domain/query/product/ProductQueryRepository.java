package com.loopers.domain.query.product;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductQueryRepository {

    Page<ProductQuery.List.Response> findProducts(Pageable pageable);
    Page<ProductQuery.List.Response> findProducts(ProductQuery.List.Condition condition, Pageable pageable);

}
