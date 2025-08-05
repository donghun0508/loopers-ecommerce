package com.loopers.domain.order;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.order.OrderCreateCommand.OrderItem;
import com.loopers.domain.shared.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@UnitTest
class OrderCreateCommandTest {


    @DisplayName("주문 생성 시, 사용자 ID가 null인 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenUserIdIsNull(Long invalidUserId) {
        List<OrderItem> orderItems = List.of(new OrderItem(1L, Money.of(1L), Quantity.of(1L)));

        assertThatThrownBy(() -> new OrderCreateCommand(invalidUserId, orderItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 시, 주문 항목이 null 또는 비어있는 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenOrderItemsIsNullOrEmpty(List<OrderItem> invalidOrderItems) {
        Long validUserId = 1L;

        assertThatThrownBy(() -> new OrderCreateCommand(validUserId, invalidOrderItems))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목 생성 시, 상품 ID가 null인 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenProductIdIsNull(Long invalidProductId) {
        Money price = Money.of(1L);
        Quantity quantity = Quantity.of(1L);

        assertThatThrownBy(() -> new OrderItem(invalidProductId, price, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목 생성 시, 주문 ID가 null인 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenOrderIdIsNull(Money invalidPrice) {
        Long validProductId = 1L;
        Quantity quantity = Quantity.of(1L);

        assertThatThrownBy(() -> new OrderItem(validProductId, invalidPrice, quantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목 생성 시, 수량이 null인 경우 예외를 반환한다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenQuantityIsNull(Quantity invalidQuantity) {
        Long validProductId = 1L;
        Money price = Money.of(1L);

        assertThatThrownBy(() -> new OrderItem(validProductId, price, invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 요청 생성 시, 데이터가 올바른 경우 정상적으로 생성된다.")
    @Test
    void createOrderCreateCommand_whenValidData() {
        Long validBuyerId = 1L;
        List<OrderItem> validOrderItems = List.of(
                new OrderItem(1L, Money.of(1L), Quantity.of(1L)),
                new OrderItem(2L, Money.of(1L), Quantity.of(2L))
        );

        OrderCreateCommand command = new OrderCreateCommand(validBuyerId, validOrderItems);

        assertThat(command.buyerId()).isEqualTo(validBuyerId);
        assertThat(command.items())
                .hasSize(2)
                .containsExactlyElementsOf(validOrderItems);
    }
}
