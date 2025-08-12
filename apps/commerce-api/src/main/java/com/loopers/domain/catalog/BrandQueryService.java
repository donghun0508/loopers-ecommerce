package com.loopers.domain.catalog;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BrandQueryService {

    public Brand getBrandDetail(Long brandId) {
        return null;
    }

    public List<Brand> findAllByProductId(List<Long> list) {
        return null;
    }
}
