package com.loopers.domain.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public boolean existsById(Long productId) {
        return productRepository.existsById(productId);
    }

    public List<Product> findAll(List<Long> productIds) {
        return productRepository.findAllByIdIn(productIds);
    }
}
