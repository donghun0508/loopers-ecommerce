package com.loopers.domain.catalog.vo;

import com.loopers.domain.catalog.entity.Product;
import com.loopers.domain.shared.Money;

import java.util.List;

public record StockRecord(List<ProductSnapshot> snapshots) {

    public static StockRecord of(List<ProductSnapshot> snapshots) {
        return new StockRecord(snapshots);
    }

    public Money calculateTotalAmount() {
        return snapshots.stream()
                .map(ProductSnapshot::totalPrice)
                .reduce(Money.of(0L), Money::add);
    }

    public record ProductSnapshot(Long productId, Long unitPrice, Long quantity) {
        public Money totalPrice() {
            return Money.of(unitPrice).multiply(quantity);
        }

        public static ProductSnapshot of(Product product, Stock quantity) {
            return new ProductSnapshot(
                    product.getId(),
                    product.getUnitPrice().value(),
                    quantity.count()
            );
        }
    }
}
