package com.loopers.domain.catalog;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    private String name;

    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    @AttributeOverride(name = "count", column = @Column(name = "stock"))
    private Stock stock;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    public void decreaseStock(Stock stock) {
        requireNonNull(stock, "수량은 null일 수 없습니다.");
        this.stock = this.stock.subtract(stock);
    }

    public Money calculatePrice(Stock stock) {
        requireNonNull(stock, "수량은 null일 수 없습니다.");
        return this.price.multiply(stock.count());
    }

}
