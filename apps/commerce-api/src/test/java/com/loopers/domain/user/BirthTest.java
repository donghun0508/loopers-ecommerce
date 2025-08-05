package com.loopers.domain.user;

import com.loopers.config.annotations.UnitTest;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static com.loopers.util.TestArguments.args;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class BirthTest {

    @DisplayName("생년월일이 null 또는 비어있는 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void throwsException_whenBirthIsNullOrEmpty(String invalidBirth) {
        assertThatThrownBy(() -> Birth.of(invalidBirth))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생년월일이 유효하지 않은 형식인 경우 예외를 반환한다.")
    @ParameterizedTest
    @MethodSource("invalidBirths")
    void throwsException_whenBirthIsInvalid(String invalidBirth) {
        assertThatThrownBy(() -> Birth.of(invalidBirth))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("생년월일이 유효한 형식인 경우 Birth 객체를 생성한다.")
    @Test
    void createBirth_whenValid() {
        var validBirth = Instancio.of(String.class)
                .generate(Select.root(), gen -> gen.temporal().localDate()
                        .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
                        .as(LocalDate::toString))
                .create();

        var birth = Birth.of(validBirth);

        assertThat(birth).isNotNull();
        assertThat(birth.day()).isEqualTo(validBirth);
    }

    static Stream<Arguments> invalidBirths() {
        return Stream.of(
                args("1990/01/01", "1990.01.01", "19900101", "1990-1-1"),
                args("90-01-01", "19900-01-01"),
                args("1990-00-01", "1990-13-01", "1990-01-00", "1990-01-32"),
                args("1990-02-30", "1990-04-31", "1990-02-29"),
                args("abcd-01-01", "1990-ab-01", "1990년-01월-01일"),
                args(" 1990-01-01", "1990-01-01 ", "1990 - 01 - 01")
        ).flatMap(stream -> stream);
    }
}
