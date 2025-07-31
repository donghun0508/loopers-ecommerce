package com.loopers.domain.heart;

import jakarta.persistence.Embeddable;

@Embeddable
public record Target(Long targetId, TargetType targetType) {

    public Target {
        if (targetId == null) {
            throw new IllegalArgumentException("대상 ID가 null일 수 없습니다.");
        }
        if (targetType == null) {
            throw new IllegalArgumentException("대상 타입이 null일 수 없습니다.");
        }
    }

    public static Target of(Long targetId, TargetType targetType) {
        return new Target(targetId, targetType);
    }

}
