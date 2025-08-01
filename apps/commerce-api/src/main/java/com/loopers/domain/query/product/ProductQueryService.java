package com.loopers.domain.query.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductQueryService {

    private final ProductQueryRepository productQueryRepository;

    public Page<ProductQuery.List.Response> findProducts(ProductQuery.List.Condition condition, Pageable pageable) {
        return productQueryRepository.findProducts(condition, pageable);
    }
}
