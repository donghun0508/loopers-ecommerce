package com.loopers.domain.catalog.service;

import com.loopers.domain.catalog.entity.Product;
import com.loopers.domain.catalog.vo.Stock;
import com.loopers.domain.catalog.vo.StockRecord;
import com.loopers.domain.catalog.vo.StockRecord.ProductSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.loopers.domain.shared.Preconditions.requireNonEmpty;
import static java.util.Objects.isNull;

public class ProductManager {

    private final List<Product> products;
    private final Map<Long, Stock> purchaseProducts;

    private ProductManager(List<Product> products, Map<Long, Stock> purchaseProducts) {
        this.products = products;
        this.purchaseProducts = purchaseProducts;
    }

    public static ProductManager assign(List<Product> products, Map<Long, Stock> purchaseProducts) {
        requireNonEmpty(products, "ProductManager.assign().products: 상품 목록은 비어있을 수 없습니다.");
        requireNonEmpty(purchaseProducts, "ProductManager.assign().purchaseProducts: 구매할 상품 목록은 비어있을 수 없습니다.");
        return new ProductManager(products, purchaseProducts);
    }

    public StockRecord decreaseStock() {
        List<ProductSnapshot> snapshots = new ArrayList<>();

        for (Product product : products) {
            Stock purchaseQuantity = purchaseProducts.get(product.getId());
            if (isNull(purchaseQuantity)) {
                throw new IllegalStateException("ProductManager.decreaseStock(): 구매할 상품이 존재하지 않습니다.");
            }
            product.decreaseStock(purchaseQuantity);
            snapshots.add(ProductSnapshot.of(product, purchaseQuantity));
        }

        return StockRecord.of(snapshots);
    }
}
