package com.loopers.domain.user;

import com.loopers.config.annotations.UnitTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class UserCreateCommandTest {

    @DisplayName("회원 ID가 null 또는 비어있는 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenAccountIdIsNull(AccountId invalidAccountId) {
        assertThatThrownBy(() -> new UserCreateCommand(invalidAccountId, Email.of("test@gmail.com"), Birth.of("19900101"), Gender.FEMALE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("이메일이 null 또는 비어있는 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenEmailIsNull(Email invalidEmail) {
        assertThatThrownBy(() -> new UserCreateCommand(AccountId.of("testUser"), invalidEmail, Birth.of("19900101"), Gender.FEMALE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생년월일이 null 또는 비어있는 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenBirthIsNull(Birth invalidBirth) {
        assertThatThrownBy(() -> new UserCreateCommand(AccountId.of("testUser"), Email.of("test@gmail.com"), invalidBirth, Gender.MALE))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("성별이 null인 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenGender(Gender invalidGender) {
        assertThatThrownBy(() -> new UserCreateCommand(AccountId.of("testUser"), Email.of("test@gamil.com"), Birth.of("19900101"), invalidGender))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
