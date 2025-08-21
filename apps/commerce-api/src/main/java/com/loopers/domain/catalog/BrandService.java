package com.loopers.domain.catalog;

import com.loopers.logging.execution.ExecutionTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class BrandService {

    private final BrandRepository brandRepository;

    @Transactional(readOnly = true)
    public Brand getBrandDetail(Long brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new IllegalArgumentException("브랜드를 찾을 수 없습니다. ID: " + brandId));
    }

    @ExecutionTime
    @Transactional(readOnly = true)
    public List<Brand> findAllById(List<Long> brandIds) {
        return brandRepository.findAllById(brandIds);
    }
}
