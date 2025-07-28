package com.loopers.domain.order;

import com.loopers.domain.shared.Money;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
public class OrderLines {

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrderLine> lines;

    OrderLines(List<OrderLine> lines) {
        this.lines = new ArrayList<>(lines);
    }

    static OrderLines empty() {
        return new OrderLines(Collections.emptyList());
    }

    public static OrderLines of(List<OrderLine> orderLines) {
        return new OrderLines(orderLines);
    }

    void add(OrderLine orderLine) {
        validateDuplicateProduct(orderLine);
        this.lines.add(orderLine);
    }

    Money calculateTotalPrice() {
        return lines.stream()
            .map(OrderLine::calculateLineTotal)
            .reduce(Money.ZERO, Money::add);
    }

    private void validateDuplicateProduct(OrderLine orderLine) {
        boolean exists = lines.stream().anyMatch(line -> line.hasSameProduct(orderLine));

        if (exists) {
            throw new IllegalStateException("이미 동일한 상품이 주문 목록에 존재합니다. " + orderLine);
        }
    }

    public Integer size() {
        return this.lines.size();
    }
}
