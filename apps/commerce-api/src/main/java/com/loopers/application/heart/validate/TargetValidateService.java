package com.loopers.application.heart.validate;

import com.loopers.domain.heart.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TargetValidateService {

    private final List<TargetValidate> targetValidates;

    public void validate(Target target) {
        for (TargetValidate targetValidate : targetValidates) {
            if (targetValidate.supports(target.targetType())) {
                targetValidate.validTargetId(target.targetId());
                return;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 TargetType입니다: " + target.targetType());
    }
}
