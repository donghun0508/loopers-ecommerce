package com.loopers.domain.heart;

import com.loopers.config.annotations.UnitTest;
import com.loopers.fixture.HeartCreateCommandFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class HeartTest {

    @DisplayName("좋아요 도메인 생성 시, ")
    @Nested
    class Create {

        @DisplayName("좋아요 생성 명령이 null인 경우 예외를 반환한다.")
        @ParameterizedTest
        @NullSource
        void throwsException_whenCommandIsNull(HeartCreateCommand invalidCommand) {
            assertThatThrownBy(() -> Heart.from(invalidCommand))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("유효한 좋아요 생성 명령을 전달하면 좋아요 도메인을 생성한다.")
        @Test
        void createHeart() {
            var command = HeartCreateCommandFixture.builder().build();

            var heart = Heart.from(command);

            assertThat(heart).isNotNull();
            assertThat(heart.getUserId()).isEqualTo(command.userId());
            assertThat(heart.getTarget()).isEqualTo(command.target());
        }
    }

}
