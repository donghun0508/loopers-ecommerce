package com.loopers.domain.heart;

public interface HeartQueryRepository {

    boolean existsByUserIdAndTarget(Long userId, Target target);
}
