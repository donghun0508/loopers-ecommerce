package com.loopers.domain.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.loopers.domain.order.OrderCommand.OrderLine.AddCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class OrderCommandTest {

    @DisplayName("주문 항목")
    @Nested
    class AddCommandOrderLineRequestTest {

        @DisplayName("생성 시")
        @Nested
        class Create {

            @DisplayName("상품 Id가 null인 경우 예외를 발생시킨다.")
            @Test
            void throwsException_whenProductIdIsNull() {
                assertThatThrownBy(() -> AddCommand.builder()
                    .productId(null)
                    .build())
                    .isInstanceOf(NullPointerException.class);
            }


            @DisplayName("상품 가격이 null인 경우 예외를 발생시킨다.")
            @Test
            void throwsException_whenPriceIsNull() {
                assertThatThrownBy(() -> AddCommand.builder()
                    .productId(1L)
                    .price(null)
                    .build())
                    .isInstanceOf(NullPointerException.class);
            }

            @DisplayName("제품 수량이 null인 경우 예외를 발생시킨다.")
            @Test
            void throwsException_whenQuantityIsNull() {
                assertThatThrownBy(() -> AddCommand.builder()
                    .productId(1L)
                    .price(1000L)
                    .quantity(null)
                    .build())
                    .isInstanceOf(NullPointerException.class);
            }
        }
    }


}
