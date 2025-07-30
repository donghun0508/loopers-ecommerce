package com.loopers.domain.heart;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.loopers.domain.fixture.HeartCommandFixture;
import com.loopers.environment.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

@UnitTest
class HeartCommandTest {

    @DisplayName("좋아요 객체 생성 시, ")
    @Nested
    class Create {

        @DisplayName("좋아요 타입이 null인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullSource
        void throwsInvalidException_whenHeartTypeIsNull(Long invalidUserId) {
            assertThatThrownBy(() -> HeartCommandFixture.Create.builder().withUserId(invalidUserId).build())
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("좋아요 타겟 ID가 null인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullSource
        void throwsInvalidException_whenTargetIdIsNull(Long invalidTargetId) {
            assertThatThrownBy(() -> HeartCommandFixture.Create.builder()
                .withUserId(1L)
                .withTargetId(invalidTargetId)
                .build())
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("좋아요 타겟 타입이 null인 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @NullSource
        void throwsInvalidException_whenTargetTypeIsNull(TargetType invalidTargetType) {
            assertThatThrownBy(() -> HeartCommandFixture.Create.builder()
                .withUserId(1L)
                .withTargetId(1L)
                .withTargetType(invalidTargetType)
                .build())
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("좋아요 객체를 생성한다.")
        @Test
        void createHeartCommand_whenValidParameters() {
            Long userId = 1L;
            Long targetId = 1L;
            TargetType targetType = TargetType.PRODUCT;

            HeartCommand.Create command = HeartCommand.Create.builder()
                .userId(userId)
                .targetId(targetId)
                .targetType(targetType)
                .build();

            assertThat(command.targetId()).isEqualTo(targetId);
            assertThat(command.userId()).isEqualTo(userId);
            assertThat(command.targetType()).isEqualTo(targetType);
        }


    }

}
