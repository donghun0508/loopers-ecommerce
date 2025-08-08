package com.loopers.domain.coupon;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class FixedDiscountConstraintsFixtureTest {

    @DisplayName("원가가 null 일 때, 예외가 발생한다")
    @ParameterizedTest
    @NullSource
    void throwsException_whenOrderAmountIsNull(Money invalidOrderAmount) {
        FixedDiscountPolicy calculator = new FixedDiscountPolicy();

        assertThatThrownBy(() -> calculator.calculateDiscount(invalidOrderAmount, 100L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("할인 금액이 null 일 때, 예외가 발생한다")
    @ParameterizedTest
    @NullSource
    void throwsException_whenDiscountValueIsNull(Long invalidDiscountValue) {
        FixedDiscountPolicy calculator = new FixedDiscountPolicy();

        assertThatThrownBy(() -> calculator.calculateDiscount(Money.of(1000L), invalidDiscountValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("할인 금액이 0 이하일 때, 예외가 발생한다")
    @ParameterizedTest
    @ValueSource(longs = {0L, -1L, -1000L})
    void throwsException_whenDiscountValueIsZeroOrNegative(Long invalidDiscountValue) {
        FixedDiscountPolicy calculator = new FixedDiscountPolicy();

        assertThatThrownBy(() -> calculator.calculateDiscount(Money.of(1000L), invalidDiscountValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("할인 금액이 원가보다 클 경우, 0원을 반환한다")
    @Test
    void returnsZero_whenDiscountValueExceedsOrderAmount() {
        FixedDiscountPolicy calculator = new FixedDiscountPolicy();
        Money orderAmount = Money.of(1000L);
        Long discountValue = 1500L;

        Money result = calculator.calculateDiscount(orderAmount, discountValue);

        assertThat(result).isEqualTo(Money.ZERO);
    }

    @DisplayName("할인 금액이 원가보다 작을 경우, 할인된 금액을 반환한다")
    @Test
    void returnsDiscountedAmount_whenDiscountValueIsLessThanOrderAmount() {
        Money orderAmount = Money.of(1000L);

        Long discountValue = 300L;

        FixedDiscountPolicy calculator = new FixedDiscountPolicy();

        Money result = calculator.calculateDiscount(orderAmount, discountValue);

        assertThat(result).isEqualTo(Money.of(orderAmount.value() - discountValue));
    }
}
