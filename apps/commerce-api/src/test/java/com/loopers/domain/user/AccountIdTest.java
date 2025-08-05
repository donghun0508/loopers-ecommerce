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
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class AccountIdTest {

    @DisplayName("계정 ID가 null 또는 비어있는 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throwsException_whenAccountIdIsNull(String invalidAccountId) {
        assertThrows(IllegalArgumentException.class, () -> AccountId.of(invalidAccountId));
    }

    @DisplayName("ID 가 `영문 및 숫자 10자 이내` 형식에 맞지 않으면, AccountId 객체 생성에 실패한다.")
    @ParameterizedTest
    @MethodSource("invalidAccountIds")
    void throwsInvalidException_whenUserIdIsInvalid(String invalidAccountId) {
        assertThrows(IllegalArgumentException.class, () -> AccountId.of(invalidAccountId));
    }

    @DisplayName("ID 가 `영문 및 숫자 10자 이내` 형식에 맞으면, AccountId 객체 생성에 성공한다.")
    @Test
    void createAccountId_whenValid() {
        var validAccountId = Instancio.of(AccountId.class)
                .generate(field(AccountId::value), gen -> gen.string()
                        .minLength(1)
                        .maxLength(10)
                        .allowEmpty(false))
                .create()
                .value();

        var accountId = AccountId.of(validAccountId);

        assertNotNull(accountId);
        assertTrue(accountId.value().matches("^[a-zA-Z0-9]{1,10}$"));
    }

    static Stream<Arguments> invalidAccountIds() {
        return Stream.of(
                args("abcdefghijk", "12345678901", "abc1234567890"),
                args("abc@123", "test.user", "user-123", "user_name"),
                args("한글abc", "test한글"),
                args("test 123", " test123", "test123 ")
        ).flatMap(stream -> stream);
    }
}
