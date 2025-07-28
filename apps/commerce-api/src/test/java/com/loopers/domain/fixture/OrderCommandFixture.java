package com.loopers.domain.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.order.OrderCommand;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class OrderCommandFixture {

    public static class OrderLine {

        public static OrderLineBuilder builder() {
            return new OrderLineBuilder();
        }

        public static class OrderLineBuilder {

            private InstancioApi<OrderCommand.OrderLine> api;

            public OrderLineBuilder() {
                this.api = Instancio.of(OrderCommand.OrderLine.class);
            }

            public OrderLineBuilder withProductId(Long productId) {
                this.api = this.api.set(field(OrderCommand.OrderLine::productId), productId);
                return this;
            }

            public OrderLineBuilder withPrice(Long price) {
                this.api = this.api.set(field(OrderCommand.OrderLine::price), price);
                return this;
            }

            public OrderLineBuilder withQuantity(Long quantity) {
                this.api = this.api.set(field(OrderCommand.OrderLine::quantity), quantity);
                return this;
            }

            public OrderCommand.OrderLine build() {
                return this.api.create();
            }
        }
    }
}
