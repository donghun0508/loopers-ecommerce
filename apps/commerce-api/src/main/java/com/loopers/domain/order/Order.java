package com.loopers.domain.order;


import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

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
    private Long buyerId;

    @Embedded
    private OrderLines orderLines;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @AttributeOverrides({
            @AttributeOverride(name = "issuedCouponId", column = @Column(name = "issued_coupon_id")),
            @AttributeOverride(name = "totalPrice.value", column = @Column(name = "total_price", nullable = false)),
            @AttributeOverride(name = "paidPrice.value", column = @Column(name = "paid_price", nullable = false))
    })
    private OrderPayment orderPayment;

    public static Order from(OrderCreateCommand command) {
        requireNonNull(command, "주문 생성 명령은 null일 수 없습니다.");

        Order order = new Order();
        order.buyerId = command.buyerId();
        order.orderLines = OrderLines.of(order, command.items());
        order.status = OrderStatus.PENDING;
        order.orderPayment = OrderPayment.before(order.orderLines.calculateTotalAmount());

        return order;
    }

    public void applyCoupon(ApplyCouponCommand command) {
        this.orderPayment = OrderPayment.after(command.couponId(), this.orderPayment.totalPrice(), command.paidAmount());
    }

    public void complete() {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("주문 상태가 결제 가능한 상태가 아닙니다.");
        }

        this.status = OrderStatus.PAID;
    }

    public Money paidAmount() {
        return this.orderPayment.paidPrice();
    }

}
