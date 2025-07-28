package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.loopers.domain.fixture.OrderCommandFixture;
import com.loopers.domain.fixture.OrderFixture;
import com.loopers.domain.fixture.UserFixture;
import com.loopers.domain.order.OrderCommand.AddOrderLineCommand;
import com.loopers.domain.user.User;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

class OrderTest {

    @DisplayName("주문 객체 생성 시,")
    @Nested
    class Create {

        @DisplayName("회원 PK값이 null인 경우 예외를 반환한다.")
        @ParameterizedTest
        @NullSource
        void throwException_whenUserIdIsNull(Long invalidUserId) {
            assertThatThrownBy(() -> Order.create(invalidUserId))
                .isInstanceOf(NullPointerException.class);
        }

        @DisplayName("회원 PK값이 null이 아닌 경우 주문 객체를 생성한다.")
        @Test
        void createOrder_whenUserIdIsValid() {
            User user = UserFixture.builder().build();

            Order order = Order.create(user.getId());

            assertThat(order).isNotNull();
            assertThat(order.getUserId()).isEqualTo(user.getId());
        }
    }

    @DisplayName("주문 항목 추가 시,")
    @Nested
    class AddOrderLineCommandOrderLineRequest {

        @DisplayName("주문 수량이 0이하인 경우 예외를 반환한다.")
        @ParameterizedTest
        @ValueSource(
            longs = {0L, -1L, -999L}
        )
        void throwException_whenQuantityIsZeroOrNegative(Long invalidQuantity) {
            Order order = OrderFixture.builder().build();
            AddOrderLineCommand addOrderLineRequestCommand = OrderCommandFixture.OrderLine.builder().withQuantity(invalidQuantity).build();

            assertThatThrownBy(() -> order.addOrderLine(addOrderLineRequestCommand))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("동일한 상품 ID로 주문 항목을 중복 추가할 경우 예외를 발생시킨다.")
        @Test
        void throwException_whenAddingDuplicateProductId() {
            Long duplicateProductId = 1L;
            Order order = OrderFixture.builder().build();
            AddOrderLineCommand addOrderLineRequest1 = OrderCommandFixture.OrderLine.builder().withProductId(duplicateProductId).withQuantity(100L).build();
            AddOrderLineCommand addOrderLineRequest2 = OrderCommandFixture.OrderLine.builder().withProductId(duplicateProductId).withQuantity(100L).build();

            order.addOrderLine(addOrderLineRequest1);

            assertThatThrownBy(() -> order.addOrderLine(addOrderLineRequest2))
                .isInstanceOf(IllegalStateException.class);
        }

        @DisplayName("주문 항목을 여러 개 추가 시 주문 라인 개수가 정확히 증가한다.")
        @ParameterizedTest
        @MethodSource("generateRandomOrderLineCounts")
        void addOrderLine_increasesOrderLineCount(Integer orderLineCount) {
            Order order = OrderFixture.builder().withEmptyOrderLine().build();

            for(int i = 0; i < orderLineCount; i++) {
                AddOrderLineCommand addOrderLineRequest = OrderCommandFixture.OrderLine.builder().build();
                order.addOrderLine(addOrderLineRequest);
            }

            assertThat(order.getOrderLineCount()).isEqualTo(orderLineCount);
        }

        @DisplayName("여러 주문 항목 추가 시 총 금액이 정확히 계산된다.")
        @ParameterizedTest
        @MethodSource("generateRandomOrderLineCounts")
        void calculateTotalAmount_whenOrderLinesAreAdded(Integer orderLineCount) {
            Order order = OrderFixture.builder().withEmptyOrderLine().build();
            Long totalAmount = 0L;

            for(int i = 0; i < orderLineCount; i++) {
                AddOrderLineCommand addOrderLineRequest = OrderCommandFixture.OrderLine.builder().build();
                order.addOrderLine(addOrderLineRequest);
                totalAmount += (addOrderLineRequest.price() * addOrderLineRequest.quantity());
            }

            assertThat(order.getTotalAmount().value()).isEqualTo(totalAmount);
        }

        static Stream<Integer> generateRandomOrderLineCounts() {
            return Stream.of(ThreadLocalRandom.current().nextInt(1, 21));
        }
    }
}
