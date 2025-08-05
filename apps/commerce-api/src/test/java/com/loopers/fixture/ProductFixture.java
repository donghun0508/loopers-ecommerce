package com.loopers.fixture;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.Stock;
import com.loopers.domain.shared.Money;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class ProductFixture {

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static ProductBuilder persistence() {
        return new ProductBuilder()
                .id(null);
    }

    public static class ProductBuilder {

        private final InstancioApi<Product> api;

        public ProductBuilder() {
            this.api = Instancio.of(Product.class);
        }

        public ProductBuilder id(Long id) {
            this.api.set(field(Product::getId), id);
            return this;
        }

        public ProductBuilder stock(Stock stock) {
            this.api.set(field(Stock::count), stock);
            return this;
        }

        public ProductBuilder price(Money price) {
            this.api.set(field(Money::value), price);
            return this;
        }

        public Product build() {
            return this.api.create();
        }
    }
}
