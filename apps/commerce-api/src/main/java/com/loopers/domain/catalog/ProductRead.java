package com.loopers.domain.catalog;

import com.loopers.domain.catalog.entity.Product;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRead {

    private Long id;
    private String name;
    private Long unitPrice;
    private Long stock;
    private Long heartCount;
    private Long brandId;
    private String brandName;
    private boolean isSoldOut;
    private boolean isLiked;

    public ProductRead(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.unitPrice = product.getUnitPrice().value();
        this.stock = product.getStock().count();
        this.heartCount = product.getHeartCount();
        this.brandId = product.getBrand().getId();
        this.brandName = product.getBrand().getName();
        this.isSoldOut = product.isSoldOut();
    }
}
