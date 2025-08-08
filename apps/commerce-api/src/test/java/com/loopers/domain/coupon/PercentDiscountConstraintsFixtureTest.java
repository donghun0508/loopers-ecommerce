package com.loopers.domain.coupon;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@UnitTest
class PercentDiscountConstraintsFixtureTest {

    @DisplayName("원가가 null인 경우, 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void throwsException_WhenOrderAmountIsNull(Money invalidOrderAmount) {
        PercentDiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy();

        assertThatThrownBy(() -> percentDiscountPolicy.calculateDiscount(invalidOrderAmount, 10L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("할인율이 null인 경우, 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void throwsException_WhenDiscountValueIsNull(Long invalidDiscountValue) {
        PercentDiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy();

        assertThatThrownBy(() -> percentDiscountPolicy.calculateDiscount(Money.of(1000L), invalidDiscountValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("할인율이 0 미만인 경우, 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(longs = {-1L, -10L})
    void throwsException_WhenDiscountValueIsZeroOrNegative(Long invalidDiscountValue) {
        PercentDiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy();

        assertThatThrownBy(() -> percentDiscountPolicy.calculateDiscount(Money.of(1000L), invalidDiscountValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("할인율이 100을 초과하는 경우, 예외를 발생시킨다.")
    @ParameterizedTest
    @ValueSource(longs = {101L, 150L})
    void throwsException_WhenDiscountValueExceedsHundred(Long invalidDiscountValue) {
        PercentDiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy();

        assertThatThrownBy(() -> percentDiscountPolicy.calculateDiscount(Money.of(1000L), invalidDiscountValue))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("할인율이 0인 경우, 할인된 결과값이 원가와 동일해야 한다.")
    @Test
    void returnsZeroDiscount_WhenDiscountValueIsZero() {
        PercentDiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy();
        Money orderAmount = Money.of(1000L);
        Long discountValue = 0L;

        Money discountAmount = percentDiscountPolicy.calculateDiscount(orderAmount, discountValue);

        assertThat(discountAmount).isEqualTo(orderAmount);
    }

    @DisplayName("할인율이 100인 경우, 할인된 결과값이 0이어야 한다.")
    @Test
    void returnsZeroDiscount_WhenDiscountValueIsHundred() {
        PercentDiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy();
        Money orderAmount = Money.of(1000L);
        Long discountValue = 100L;

        Money discountAmount = percentDiscountPolicy.calculateDiscount(orderAmount, discountValue);

        assertThat(discountAmount).isEqualTo(Money.ZERO);
    }

    @DisplayName("할인율이 50인 경우, 할인된 결과값이 원가의 절반이어야 한다.")
    @Test
    void returnsHalfDiscount_WhenDiscountValueIsFifty() {
        PercentDiscountPolicy percentDiscountPolicy = new PercentDiscountPolicy();
        Money orderAmount = Money.of(1000L);
        Long discountValue = 50L;

        Money discountAmount = percentDiscountPolicy.calculateDiscount(orderAmount, discountValue);

        assertThat(discountAmount).isEqualTo(orderAmount.divide(2L));
    }

}
