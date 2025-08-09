package com.loopers.domain.order;

import com.loopers.config.annotations.UnitTest;
import com.loopers.domain.shared.Money;
import com.loopers.fixture.OrderCreateCommandFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@UnitTest
class OrderTest {

    @DisplayName("주문 도메인 생성 시, ")
    @Nested
    class Create {

        @DisplayName("주문 생성 명령이 null인 경우 예외를 반환한다.")
        @ParameterizedTest
        @NullSource
        void throwsException_whenCommandIsNull(OrderCreateCommand invalidCommand) {
            assertThrows(IllegalArgumentException.class, () -> Order.from(invalidCommand));
        }

        @DisplayName("유효한 주문 생성 명령을 전달하면 주문 도메인을 생성한다.")
        @Test
        void createOrder() {
            OrderCreateCommand command = OrderCreateCommandFixture.builder().build();

            Order order = Order.from(command);

            assertThat(order).isNotNull();
            assertThat(order.getBuyerId()).isEqualTo(command.buyerId());

            assertThat(order.getOrderLines().getLines())
                    .hasSize(command.items().size())
                    .zipSatisfy(command.items(), (orderLine, orderItem) -> {
                        assertThat(orderLine.getProductId()).isEqualTo(orderItem.productId());
                        assertThat(orderLine.getPrice().value()).isEqualTo(orderItem.price().value());
                        assertThat(orderLine.getQuantity().count()).isEqualTo(orderItem.quantity().count());
                    });
        }

        @DisplayName("유효한 주문 생성 명령을 전달한 경우, 주문 도메인의 총 금액이 올바르게 계산된다.")
        @Test
        void calculateTotalPrice() {
            OrderCreateCommand command = OrderCreateCommandFixture.builder().build();

            Order order = Order.from(command);

            Money expectedTotalPrice = command.items().stream()
                    .map(item -> item.price().multiply(item.quantity().count()))
                    .reduce(Money.ZERO, Money::add);

            assertThat(order.paidAmount()).isEqualTo(expectedTotalPrice);
        }
    }
}
