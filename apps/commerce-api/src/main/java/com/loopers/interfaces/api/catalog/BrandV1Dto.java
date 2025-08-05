package com.loopers.interfaces.api.catalog;

import com.loopers.application.catalog.Results;

public class BrandV1Dto {

    public record GetDetail(Long brandId, String brandName, Long totalProductCount) {

        public static GetDetail from(Results.GetBrandDetailResult result) {
            return new GetDetail(
                    result.brandId(),
                    result.brandName(),
                    result.totalProductCount()
            );
        }
    }

}
