package com.loopers.domain.catalog.service;

import com.loopers.aop.execution.ExecutionTime;
import com.loopers.domain.catalog.entity.Brand;
import com.loopers.domain.catalog.repository.BrandQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BrandQueryService {

    private final BrandQueryRepository brandQueryRepository;

    public Brand getBrandDetail(Long brandId) {
        return brandQueryRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("브랜드를 찾을 수 없습니다. ID: " + brandId));
    }

    @ExecutionTime
    public List<Brand> findAllById(List<Long> brandIds) {
        return brandQueryRepository.findAllById(brandIds);
    }
}
