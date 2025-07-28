package com.loopers.domain.product;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.product.ProductCommand.DecreaseStock;
import com.loopers.domain.shared.Money;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product extends BaseEntity {

    @AttributeOverride(name = "value", column = @Column(name = "price"))
    private Money price;

    @AttributeOverride(name = "count", column = @Column(name = "stock"))
    private Stock stock;

    public void decreaseStock(DecreaseStock quantity) {
        this.stock = this.stock.subtract(quantity.quantity());
    }
}
