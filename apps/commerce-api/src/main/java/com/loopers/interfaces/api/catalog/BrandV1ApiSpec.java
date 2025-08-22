package com.loopers.interfaces.api.catalog;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Brand V1 API", description = "브랜드 관리 API 입니다.")
public interface BrandV1ApiSpec {

    @Operation(
        summary = "브랜드 조회",
        description = "브랜드 목록을 조회합니다."
    )
    ApiResponse<BrandV1Dto.GetDetail> getBrandDetail(Long brandId);

}
