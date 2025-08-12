package com.loopers.domain.catalog;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.ConditionalOnGraphQlSchema;

import static com.loopers.domain.shared.Preconditions.requireNonNull;
import static com.loopers.domain.shared.Preconditions.requirePositive;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    private String name;

    @AttributeOverride(name = "value", column = @Column(name = "unit_price"))
    private Money unitPrice;

    @AttributeOverride(name = "count", column = @Column(name = "stock"))
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @Column(name = "heart_count", nullable = false)
    private Long heartCount;

    @Version
    private Long version;

    public void decreaseStock(Stock stock) {
        requireNonNull(stock, "Product.decreaseStock().stock: 수량은 null일 수 없습니다.");
        requirePositive(stock.count(), "Product.decreaseStock().stock.count: 수량은 0보다 커야 합니다.");
        this.stock = this.stock.subtract(stock);
    }

    public void updateHeartCount() {
        this.heartCount++;
    }
}
