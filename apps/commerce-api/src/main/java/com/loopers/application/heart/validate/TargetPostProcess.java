package com.loopers.application.heart.validate;

import com.loopers.domain.heart.TargetType;

public interface TargetPostProcess {
    boolean supports(TargetType targetType);

    void process(Long targetId);
}
