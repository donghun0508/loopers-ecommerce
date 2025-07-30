package com.loopers.domain.order;

import static com.loopers.utils.ValidationUtils.requireNonEmpty;
import static com.loopers.utils.ValidationUtils.requireNonNull;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import com.loopers.domain.shared.PickingReport;
import com.loopers.domain.shared.PickingReport.PickedItem;
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

    private String orderNumber;

    @Embedded
    private OrderLines orderLines;

    public static Order create(OrderCommand.Create command, OrderNumberGenerator orderNumberGenerator) {
        requireNonNull(command, "주문 생성 명령이 null입니다.");
        requireNonNull(orderNumberGenerator, "주문 번호 생성기가 null입니다.");

        Order order = new Order();
        order.userId = command.userId();
        order.orderLines = OrderLines.empty();
        order.orderNumber = orderNumberGenerator.generate();

        requireNonNull(order.orderNumber, "주문 번호가 null입니다.");

        return order;
    }

    public void applyPickingResult(PickingReport pickingReport) {
        requireNonNull(pickingReport, "피킹 리포트가 null입니다.");
        requireNonEmpty(pickingReport.getPickedItems(), "피킹 리포트에 피킹된 아이템이 없습니다.");

        if (!orderNumber.equals(pickingReport.getOrderNumber())) {
            throw new IllegalArgumentException("주문 번호가 피킹 리포트의 주문 번호와 일치하지 않습니다.");
        }

        for (PickedItem pickedItem : pickingReport.getPickedItems()) {
            OrderLine orderLine = OrderLine.createItem(this, pickedItem);
            this.orderLines.add(orderLine);
        }
    }

    public Money getTotalAmount() {
        return this.orderLines.calculateTotalPrice();
    }

    public Integer getOrderLineCount() {
        return this.orderLines.size();
    }
}
