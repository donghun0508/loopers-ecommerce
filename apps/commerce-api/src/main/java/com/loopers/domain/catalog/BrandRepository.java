package com.loopers.domain.catalog;

import org.springframework.data.repository.Repository;

public interface BrandRepository extends Repository<Brand, Long> {
    Brand save(Brand brand);
}
