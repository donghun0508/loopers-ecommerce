package com.loopers.domain.heart;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

public record HeartCreateCommand(Long userId, Target target) {

    public HeartCreateCommand {
        requireNonNull(userId, "사용자 ID는 null일 수 없습니다");
        requireNonNull(target, "좋아요 대상은 null일 수 없습니다");
        requireNonNull(target.targetId(), "좋아요 대상 ID는 null일 수 없습니다");
        requireNonNull(target.targetType(), "좋아요 대상 타입은 null일 수 없습니다");
    }

    public static HeartCreateCommand of(Long userId, Target target) {
        return new HeartCreateCommand(userId, target);
    }
}
