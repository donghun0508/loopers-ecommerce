package com.loopers.domain.catalog.repository;

import com.loopers.domain.catalog.entity.Brand;
import org.springframework.data.repository.Repository;

public interface BrandRepository extends Repository<Brand, Long> {
    Brand save(Brand brand);
}
