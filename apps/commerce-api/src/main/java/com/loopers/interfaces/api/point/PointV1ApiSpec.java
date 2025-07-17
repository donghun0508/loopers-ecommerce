package com.loopers.interfaces.api.point;

import com.loopers.interfaces.api.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Point V1 API", description = "포인트 관리 API 입니다.")
public interface PointV1ApiSpec {

    @Operation(
        summary = "회원 가입",
        description = "새로운 회원을 등록합니다."
    )
    @Parameter(
        name = "X-USER-ID",
        description = "조회할 회원의 ID",
        required = true,
        in = ParameterIn.HEADER,
        schema = @Schema(
            type = "string",
            example = "user123"
        )
    )
    ApiResponse<PointV1Dto.PointResponse> getPoint(
        @Parameter(hidden = true)
        String userId
    );
}
