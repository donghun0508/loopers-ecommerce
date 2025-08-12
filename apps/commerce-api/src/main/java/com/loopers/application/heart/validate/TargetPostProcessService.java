package com.loopers.application.heart.validate;

import com.loopers.domain.heart.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class TargetPostProcessService {

    private final List<TargetPostProcess> targetPostProcesses;

    public void process(Target target) {
        for (TargetPostProcess targetPostProcess : targetPostProcesses) {
            if (targetPostProcess.supports(target.targetType())) {
                targetPostProcess.process(target.targetId());
                return;
            }
        }
        throw new IllegalArgumentException("지원하지 않는 TargetType입니다: " + target.targetType());
    }
}
