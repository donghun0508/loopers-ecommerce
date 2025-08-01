package com.loopers.infrastructure.product;

import com.loopers.domain.query.product.ProductQuery.List.Condition;
import com.loopers.domain.query.product.ProductQuery.List.Response;
import com.loopers.domain.query.product.ProductQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class ProductQueryRepositoryImpl implements ProductQueryRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Page<Response> findProducts(Pageable pageable) {
        return findProducts(Condition.ofDefault(), pageable);
    }

    @Override
    public Page<Response> findProducts(Condition condition, Pageable pageable) {
        return productJpaRepository.findProducts(condition, pageable);
    }
}
