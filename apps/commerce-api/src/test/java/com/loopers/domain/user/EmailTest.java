package com.loopers.domain.user;

import com.loopers.config.annotations.UnitTest;
import org.instancio.Instancio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.util.stream.Stream;

import static com.loopers.util.TestArguments.args;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class EmailTest {

    @DisplayName("이메일이 null 또는 비어있는 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throwsException_whenEmailIsNullOrEmpty(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () -> Email.of(invalidEmail));
    }

    @DisplayName("이메일 형식이 유효하지 않은 경우 예외를 반환한다.")
    @ParameterizedTest
    @MethodSource("invalidEmails")
    void throwsInvalidException_whenEmailIsInvalid(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () -> Email.of(invalidEmail));
    }

    @DisplayName("이메일 형식이 유효한 경우 Email 객체를 생성한다.")
    @Test
    void createEmail_whenValid() {
        var validEmail = Instancio.gen().net().email().get();

        var email = Email.of(validEmail);

        assertNotNull(email);
        assertThat(email.address()).isEqualTo(validEmail);
    }

    static Stream<Arguments> invalidEmails() {
        return Stream.of(
                args("testuser.com", "test@@example.com", "@example.com", "test@"),
                args("test@domain", "test@.example.com", "test@example.", "test@example..com"),
                args("test user@example.com", "test..user@example.com",
                        ".testuser@example.com", "test.user.@example.com"),
                args("테스트@example.com", "test@한글.com", "test@example.c")
        ).flatMap(stream -> stream);
    }
}
