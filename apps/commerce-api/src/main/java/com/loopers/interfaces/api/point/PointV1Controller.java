package com.loopers.interfaces.api.point;

import com.loopers.application.user.CriteriaCommand.UserPointChargeCriteria;
import com.loopers.application.user.CriteriaQuery.GetUserPointCriteria;
import com.loopers.application.user.UserResults.UserPointResult;
import com.loopers.application.user.UserFacade;
import com.loopers.interfaces.api.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static com.loopers.interfaces.api.ApiHeaders.USER_ID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/points")
public class PointV1Controller implements PointV1ApiSpec {

    private final UserFacade userFacade;

    @GetMapping
    @Override
    public ApiResponse<PointV1Dto.PointResponse> getPoint(
            @RequestHeader(value = USER_ID) String userId
    ) {
        GetUserPointCriteria criteria = GetUserPointCriteria.of(userId);
        UserPointResult userPointResult = userFacade.getPoint(criteria);
        return ApiResponse.success(PointV1Dto.PointResponse.from(userPointResult));
    }

    @PostMapping("/charge")
    @Override
    public ApiResponse<PointV1Dto.PointResponse> chargePoint(
            @RequestHeader(value = USER_ID) String userId,
            @RequestBody PointV1Dto.ChargeRequest request
    ) {
        UserPointChargeCriteria criteria = UserPointChargeCriteria.of(userId, request.amount());
        UserPointResult userPointResult = userFacade.chargePoint(criteria);
        return ApiResponse.success(PointV1Dto.PointResponse.from(userPointResult));
    }
}
