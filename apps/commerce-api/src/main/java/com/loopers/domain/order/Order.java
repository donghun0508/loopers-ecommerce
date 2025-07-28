package com.loopers.domain.order;

import static java.util.Objects.requireNonNull;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseEntity {

    @Column(
        name = "user_id",
        nullable = false,
        updatable = false
    )
    private Long userId;

    @Embedded
    private OrderLines orderLines;

    public static Order create(Long userId) {
        requireNonNull(userId, "회원 Id가 null입니다.");

        Order order = new Order();
        order.userId = userId;
        order.orderLines = OrderLines.empty();

        return order;
    }

    public void addOrderLine(OrderCommand.OrderLine command) {
        OrderLine orderLine = OrderLine.createItem(this, command);
        orderLines.add(orderLine);
    }

    public Money getTotalAmount() {
        return this.orderLines.calculateTotalPrice();
    }

    public Integer getOrderLineCount() {
        return this.orderLines.size();
    }
}
