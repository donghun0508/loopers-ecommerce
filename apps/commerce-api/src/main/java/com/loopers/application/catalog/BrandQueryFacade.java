package com.loopers.application.catalog;

import com.loopers.application.catalog.CatalogResults.BrandDetailResult;
import com.loopers.domain.catalog.Brand;
import com.loopers.domain.catalog.BrandQueryService;
import com.loopers.domain.catalog.ProductQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BrandQueryFacade {

    private final BrandQueryService brandQueryService;
    private final ProductQueryService productQueryService;

    public BrandDetailResult getBrandDetail(Long brandId) {
        Brand brand = brandQueryService.getBrandDetail(brandId);
        Long productCount = productQueryService.getCountByBrandId(brandId);
        return new BrandDetailResult(brand.getId(), brand.getName(), productCount);
    }
}
