package com.loopers.interfaces.api.point;

import com.loopers.application.user.UserPointInfo;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;

public class PointV1Dto {

    @Builder(access = AccessLevel.PRIVATE)
    public record PointResponse(
        Long point
    ) {
        public static PointResponse from(UserPointInfo userPointInfo) {
            Objects.requireNonNull(userPointInfo, "UserPointInfo 객체가 null입니다.");

            return PointResponse.builder()
                .point(userPointInfo.point())
                .build();
        }
    }
}
