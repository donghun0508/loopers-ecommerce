package com.loopers.domain.fixture;

import static org.instancio.Select.all;
import static org.instancio.Select.field;

import com.loopers.domain.fixture.UserFixture.UserBuilder;
import com.loopers.domain.product.Product;
import com.loopers.domain.product.Stock;
import com.loopers.domain.shared.Money;
import com.loopers.domain.user.Point;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Scope;
import org.instancio.Select;

public class ProductFixture {

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {

        private InstancioApi<Product> api;

        public ProductBuilder() {
            this.api = Instancio.of(Product.class)

                .onComplete(all(Product.class), (Product order) -> {
                    try {
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        }

        public ProductBuilder withStock(Long stock) {
            this.api = this.api.set(field(Stock::count), stock);
            return this;
        }

        public Product build() {
            return this.api.create();
        }
    }
}
