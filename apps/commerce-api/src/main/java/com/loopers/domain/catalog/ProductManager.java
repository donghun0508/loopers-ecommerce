package com.loopers.domain.catalog;

import com.loopers.domain.shared.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class ProductManager {

    private final List<Product> products;
    private final Map<Long, Stock> purchaseProducts;
    private final List<ProductSnapshot> snapshots = new ArrayList<>();
    private Long totalPrice = 0L;

    private ProductManager(List<Product> products, Map<Long, Stock> purchaseProducts) {
        this.products = products;
        this.purchaseProducts = purchaseProducts;
    }

    public static ProductManager assign(List<Product> products, Map<Long, Stock> purchaseProducts) {
        return new ProductManager(products, purchaseProducts);
    }

    public void decreaseStock() {
        for (Product product : products) {
            if (purchaseProducts.containsKey(product.getId())) {
                Stock quantity = purchaseProducts.get(product.getId());
                if (quantity != null && quantity.count() > 0) {
                    Stock stock = Stock.of(quantity.count());
                    product.decreaseStock(stock);

                    Money price = product.calculatePrice(stock);
                    totalPrice += price.value();

                    snapshots.add(new ProductSnapshot(product.getId(), price.value(), stock.count()));
                }
            }
        }
    }

    public List<ProductSnapshot> snapshots() {
        return snapshots;
    }

    public Money totalPrice() {
        return Money.of(totalPrice);
    }

    public record ProductSnapshot(Long productId, Long price, Long quantity) {
    }
}
