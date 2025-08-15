package com.loopers.domain.catalog.repository;

import com.loopers.domain.catalog.entity.Brand;
import java.util.List;
import java.util.Optional;

public interface BrandQueryRepository {

    List<Brand> findAllById(List<Long> brandIds);

    Optional<Brand> findById(Long brandId);
}
