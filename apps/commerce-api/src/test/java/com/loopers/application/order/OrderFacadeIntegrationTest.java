package com.loopers.application.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import com.loopers.domain.fixture.OrderCommandFixture;
import com.loopers.domain.fixture.ProductFixture;
import com.loopers.domain.fixture.UserFixture;
import com.loopers.domain.order.Order;
import com.loopers.domain.order.OrderCommand;
import com.loopers.domain.order.OrderCommand.Create.OrderItem;
import com.loopers.domain.order.OrderRepository;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.ProductRepository;
import com.loopers.domain.shared.Money;
import com.loopers.domain.user.User;
import com.loopers.domain.user.UserRepository;
import com.loopers.environment.annotations.IntegrationTest;
import com.loopers.support.error.user.UserNotFoundException;
import com.loopers.utils.DatabaseCleanUp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class OrderFacadeIntegrationTest {

    @Autowired
    private OrderFacade orderFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("상품 주문 시, ")
    @Nested
    class RequestOrder {

        @DisplayName("주문 요청 시, 회원이 존재하지 않으면 예외를 발생시킨다.")
        @Test
        void throwsInvalidException_whenUserNotFound() {
            String nonExistentUserId = "no-user-id";
            OrderCommand.Create command = OrderCommandFixture.Create.builder().build();

            assertThatThrownBy(() -> orderFacade.requestOrder(nonExistentUserId, command))
                .isInstanceOf(UserNotFoundException.class);
        }

        @DisplayName("주문 요청 시, 구매할 상품이 존재하지 않으면 예외를 발생시킨다.")
        @Test
        void throwsInvalidException_whenProductNotFound() {
            User user = UserFixture.integration().build();
            userRepository.save(user);
            OrderCommand.Create command = new OrderCommand.Create(
                List.of(
                    new OrderItem(Long.MAX_VALUE, 1000L, 1000L),
                    new OrderItem(Long.MAX_VALUE - 1, 1000L, 1000L)
                )
            );

            assertThatThrownBy(() -> orderFacade.requestOrder(user.getUserId(), command))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @DisplayName("주문 요청 시, 정상적으로 주문을 생성한다.")
        @Test
        void success_whenOrderRequestIsValid() {
            // arrange
            Long chargeAmount = 10000L;
            Long totalUsePrice = 0L;

            User user = userRepository.save(UserFixture.integration().build());

            user.earnPoints(new Money(chargeAmount));
            userRepository.save(user);

            List<Product> products = productRepository.saveAll(List.of(
                ProductFixture.integration().withStock(100L).withPrice(150L).build(),
                ProductFixture.integration().withStock(200L).withPrice(200L).build()
            ));

            List<OrderItem> orderItems = products.stream()
                .map(product -> new OrderItem(
                    product.getId(),
                    product.getPrice().value(),
                    Math.max(1L, product.getStock().count() / 10)
                ))
                .toList();

            OrderCommand.Create command = new OrderCommand.Create(orderItems);
            totalUsePrice = command.totalPrice();

            // act
            orderFacade.requestOrder(user.getUserId(), command);

            // assert
            User findUser = userRepository.findByUserId(user.getUserId()).orElseThrow();
            assertThat(findUser.getPoint().getBalance().value()).isEqualTo(chargeAmount - totalUsePrice);

            Order findOrder = orderRepository.findByUserId(findUser.getId()).orElseThrow();
            assertThat(findOrder.getTotalAmount().value()).isEqualTo(totalUsePrice);
            assertThat(findOrder.getOrderLineCount()).isEqualTo(orderItems.size());
            assertThat(findOrder).satisfies(order -> {
                Map<Long, Long> actualLines = order.lines();
                orderItems.forEach(expectedItem ->
                    assertThat(actualLines.get(expectedItem.productId()))
                        .isEqualTo(expectedItem.quantity())
                );

                assertThat(actualLines).hasSize(orderItems.size());
            });

            List<Long> productIds = new ArrayList<>(findOrder.lines().keySet());
            List<Product> findProducts = productRepository.findAllById(productIds);
            assertThat(findProducts).hasSize(orderItems.size());
            Map<Long, Long> orderLines = findOrder.lines();

            for (Product findProduct : findProducts) {
                Long productId = findProduct.getId();
                Long orderedQuantity = orderLines.get(productId);

                Product originalProduct = products.stream()
                    .filter(p -> p.getId().equals(productId))
                    .findFirst()
                    .orElseThrow();

                Long expectedStock = originalProduct.getStock().count() - orderedQuantity;
                assertThat(findProduct.getStock().count()).isEqualTo(expectedStock);
                assertThat(findProduct.getPrice().value()).isEqualTo(originalProduct.getPrice().value());
            }
        }

    }

}
