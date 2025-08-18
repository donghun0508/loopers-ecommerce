package com.loopers.interfaces.api.point;

import com.loopers.application.user.UserCommand.UserChargePointCommand;
import com.loopers.application.user.UserResult.UserPointResult;
import com.loopers.application.user.UserFacade;
import com.loopers.domain.shared.Money;
import com.loopers.domain.user.AccountId;
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
        UserPointResult userPointResult = userFacade.getPoint(AccountId.of(userId));
        return ApiResponse.success(PointV1Dto.PointResponse.from(userPointResult));
    }

    @PostMapping("/charge")
    @Override
    public ApiResponse<PointV1Dto.PointResponse> chargePoint(
            @RequestHeader(value = USER_ID) String userId,
            @RequestBody PointV1Dto.ChargeRequest request
    ) {
        UserPointResult userPointResult = userFacade.chargePoint(new UserChargePointCommand(
                AccountId.of(userId),
                Money.of(request.amount())
        ));
        return ApiResponse.success(PointV1Dto.PointResponse.from(userPointResult));
    }
}
