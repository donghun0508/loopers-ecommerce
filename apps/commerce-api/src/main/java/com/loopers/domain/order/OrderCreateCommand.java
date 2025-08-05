package com.loopers.domain.order;

import com.loopers.domain.catalog.ProductManager.ProductSnapshot;
import com.loopers.domain.shared.Money;

import java.util.List;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

public record OrderCreateCommand(
        Long buyerId,
        List<OrderItem> items
) {
    public OrderCreateCommand {
        requireNonNull(buyerId, "구매자 ID는 null일 수 없습니다.");
        requireNonNull(items, "주문 항목은 null일 수 없습니다.");
    }

    public static OrderCreateCommand of(Long buyerId, List<ProductSnapshot> productSnapshots) {
        return new OrderCreateCommand(buyerId, productSnapshots.stream().map(OrderItem::new).toList());
    }


    public record OrderItem(Long productId, Money price, Quantity quantity) {
        public OrderItem {
            requireNonNull(productId, "상품 ID는 null일 수 없습니다.");
            requireNonNull(price, "가격은 null일 수 없습니다.");
            requireNonNull(quantity, "수량은 null일 수 없습니다.");
        }

        public OrderItem(Long productId, Long price, Long quantity) {
            this(
                    requireNonNull(productId, "상품 ID는 null일 수 없습니다."),
                    Money.of(requireNonNull(price, "가격은 null일 수 없습니다.")),
                    Quantity.of(requireNonNull(quantity, "수량은 null일 수 없습니다."))
            );
        }

        public OrderItem(ProductSnapshot snapshot) {
            this(snapshot.productId(), snapshot.price(), snapshot.quantity());
        }

        public static OrderItem of(Long productId, Long price, Long quantity) {
            return new OrderItem(productId, Money.of(price), Quantity.of(quantity));
        }
    }

}
