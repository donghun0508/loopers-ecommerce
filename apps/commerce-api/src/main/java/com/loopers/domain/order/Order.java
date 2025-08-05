package com.loopers.domain.order;


import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

@Getter(AccessLevel.PACKAGE)
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Column(
            name = "user_id",
            nullable = false,
            updatable = false
    )
    private Long buyerId;

    @Embedded
    private OrderLines orderLines;

    public static Order from(OrderCreateCommand command) {
        requireNonNull(command, "주문 생성 명령은 null일 수 없습니다.");

        Order order = new Order();
        order.buyerId = command.buyerId();
        order.orderLines = OrderLines.from(order, command.items());

        return order;
    }

    public Money getTotalAmount() {
        return this.orderLines.calculateTotalAmount();
    }
}
