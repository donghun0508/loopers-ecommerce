package com.loopers.domain.catalog;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface ProductRepository extends Repository<Product, Long> {

    boolean existsById(Long targetId);

    List<Product> findAllByIdIn(List<Long> productIds);
}
