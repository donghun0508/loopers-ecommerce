package com.loopers.domain.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.domain.point.fixture.PointCommandFixture;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

class PointTest {

    @DisplayName("포인트 객체 생성 시, ")
    @Nested
    class Create {

        @DisplayName("회원 ID(PK)가 Null인 경우, 포인트 객체 생성에 실패한다.")
        @ParameterizedTest
        @NullSource
        void throwsInvalidException_whenUserPkIsInvalid(Long invalidUserPk) {
            // arrange
            PointCommand.Create command = PointCommandFixture.Create.complete()
                .set(field(PointCommand.Create::userId), invalidUserPk)
                .create();

            // act
            CoreException exception = assertThrows(CoreException.class, () -> Point.of(command));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.INVALID_INPUT);
        }

        @DisplayName("회원 존재한 경우, 포인트 객체 생성에 성공한다.")
        @Test
        void returnPoint_whenUserPkValid() {
            // arrange
            PointCommand.Create command = PointCommandFixture.Create.complete().create();

            // act
            Point point = Point.of(command);

            // assert
            assertThat(point.getUserId()).isEqualTo(command.userId());
            assertThat(point.getAmount()).isZero();
        }
    }
}
