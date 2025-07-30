package com.loopers.domain.heart;

import static com.loopers.utils.ValidationUtils.requireNonNull;

import lombok.Builder;

public class HeartCommand {

    @Builder
    public record Create(
        Long userId,
        Long targetId,
        TargetType targetType
    ) {
        public Create {
            requireNonNull(userId, "사용자 ID가 null일 수 없습니다.");
            requireNonNull(targetId, "대상 ID가 null일 수 없습니다.");
            requireNonNull(targetType, "대상 타입이 null일 수 없습니다.");
        }
    }

}
