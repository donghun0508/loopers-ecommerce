package com.loopers.infrastructure.product;

import com.loopers.domain.query.product.ProductQuery;
import com.loopers.domain.query.product.ProductQuery.List.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomProductQueryRepository {

    Page<Response> findProducts(Pageable pageable);

    Page<ProductQuery.List.Response> findProducts(ProductQuery.List.Condition condition, Pageable pageable);
}
