package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.loopers.domain.user.User.Gender;
import com.loopers.domain.user.fixture.UserCommandFixture;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import com.loopers.support.error.user.PointException;
import com.loopers.support.error.user.UserErrorType;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class UserTest {

    @DisplayName("회원 명령 객체 생성 시,")
    @Nested
    class Create {

        @DisplayName("ID 가 `영문 및 숫자 10자 이내` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @ParameterizedTest
        @NullSource
        @MethodSource("invalidUserIds")
        void throwsInvalidException_whenUserIdIsInvalid(String invalidUserId) {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete()
                .set(field(UserCommand.Create::userId), invalidUserId)
                .create();

            // act
            CoreException exception = assertThrows(CoreException.class, () -> User.of(command));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.INVALID_INPUT);
        }

        @DisplayName("이메일이 `xx@yy.zz` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @ParameterizedTest
        @NullSource
        @MethodSource("invalidEmails")
        void throwsInvalidException_whenEmailIsInvalid(String invalidEmail) {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete()
                .set(field(UserCommand.Create::email), invalidEmail)
                .create();

            // act
            CoreException exception = assertThrows(CoreException.class, () -> User.of(command));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.INVALID_INPUT);
        }

        @DisplayName("생년월일이 `yyyy-MM-dd` 형식에 맞지 않으면, User 객체 생성에 실패한다.")
        @ParameterizedTest
        @NullSource
        @MethodSource("invalidBirths")
        void throwsInvalidException_whenBirthIsInvalid(String invalidBirth) {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete()
                .set(field(UserCommand.Create::birth), invalidBirth)
                .create();

            // act
            CoreException exception = assertThrows(CoreException.class, () -> User.of(command));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.INVALID_INPUT);
        }

        @DisplayName("성별이 없는 경우, User 객체 생성에 실패한다.")
        @ParameterizedTest
        @NullSource
        void throwsInvalidException_whenGenderIsNullAndEmpty(Gender invalidGender) {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete()
                .set(field(UserCommand.Create::gender), invalidGender)
                .create();

            // act
            CoreException exception = assertThrows(CoreException.class, () -> User.of(command));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.INVALID_INPUT);
        }

        @DisplayName("0 이하의 정수로 포인트를 충전 시 실패한다.")
        @ParameterizedTest
        @NullSource
        @ValueSource(longs = {0L, -1L, -100L, -999L})
        void throwsPointException_whenAmountIsZeroOrBelow(Long invalidPoint) {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();
            User user = User.of(command);

            // act
            PointException exception = assertThrows(PointException.class,
                () -> user.chargePoint(invalidPoint));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(UserErrorType.INVALID_CHARGE_AMOUNT);
        }

        static Stream<Arguments> invalidUserIds() {
            return TestDataProvider.invalidUserIds();
        }

        static Stream<Arguments> invalidEmails() {
            return TestDataProvider.invalidEmails();
        }

        static Stream<Arguments> invalidBirths() {
            return TestDataProvider.invalidBirths();
        }

        static class TestDataProvider {

            static Stream<Arguments> invalidUserIds() {
                return Stream.of(
                    emptyValues(),
                    args("abcdefghijk", "12345678901", "abc1234567890"),
                    args("abc@123", "test.user", "user-123", "user_name"),
                    args("한글abc", "test한글"),
                    args("test 123", " test123", "test123 ")
                ).flatMap(stream -> stream);
            }

            static Stream<Arguments> invalidEmails() {
                return Stream.of(
                    emptyValues(),
                    args("testuser.com", "test@@example.com", "@example.com", "test@"),
                    args("test@domain", "test@.example.com", "test@example.", "test@example..com"),
                    args("test user@example.com", "test..user@example.com",
                        ".testuser@example.com", "test.user.@example.com"),
                    args("테스트@example.com", "test@한글.com", "test@example.c")
                ).flatMap(stream -> stream);
            }

            static Stream<Arguments> invalidBirths() {
                return Stream.of(
                    emptyValues(),
                    args("1990/01/01", "1990.01.01", "19900101", "1990-1-1"),
                    args("90-01-01", "19900-01-01"),
                    args("1990-00-01", "1990-13-01", "1990-01-00", "1990-01-32"),
                    args("1990-02-30", "1990-04-31", "1990-02-29"),
                    args("abcd-01-01", "1990-ab-01", "1990년-01월-01일"),
                    args(" 1990-01-01", "1990-01-01 ", "1990 - 01 - 01")
                ).flatMap(stream -> stream);
            }

            private static Stream<Arguments> emptyValues() {
                return Stream.of("", "   ", "    ").map(Arguments::of);
            }

            private static Stream<Arguments> args(String... values) {
                return Stream.of(values).map(Arguments::of);
            }
        }
    }
}
