package com.loopers.interfaces.api.point;

import com.loopers.application.user.Results.UserPointResult;
import lombok.AccessLevel;
import lombok.Builder;

import java.util.Objects;

public class PointV1Dto {

    public record ChargeRequest(Long amount) {

    }

    @Builder(access = AccessLevel.PRIVATE)
    public record PointResponse(
            String userId,
            Long point
    ) {
        public static PointResponse from(UserPointResult userPointResult) {
            Objects.requireNonNull(userPointResult, "UserPointResult 객체가 null입니다.");

            return PointResponse.builder()
                    .userId(userPointResult.accountId())
                    .point(userPointResult.point())
                    .build();
        }
    }
}
