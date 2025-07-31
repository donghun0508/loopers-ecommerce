package com.loopers.domain.product;

import java.util.List;

public interface ProductRepository {

    List<Product> findAllById(List<Long> productIds);

    Product save(Product product);

    List<Product> saveAll(List<Product> product);

    boolean existsById(Long productId);
}
