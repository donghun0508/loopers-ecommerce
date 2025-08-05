package com.loopers.domain.heart;

import com.loopers.config.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class HeartCreateCommandTest {

    @DisplayName("좋아요 생성 시, 사용자 ID가 null인 경우 예외를 반환한다.")
    @Test
    void throwsException_whenUserIdIsNull() {
        assertThatThrownBy(() -> new HeartCreateCommand(null, Target.of(1L, TargetType.PRODUCT)))
                        .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("좋아요 생성 시, 대상 ID가 null인 경우 예외를 반환한다.")
    @Test
    void throwsException_whenTargetIdIsNull() {
        assertThatThrownBy(() -> new HeartCreateCommand(1L, Target.of(null, TargetType.PRODUCT)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("좋아요 생성 시, 대상 타입이 null인 경우 예외를 반환한다.")
    @Test
    void throwsException_whenTargetTypeIsNull() {
        assertThatThrownBy(() -> new HeartCreateCommand(1L, Target.of(1L, null)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
