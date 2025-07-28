package com.loopers.domain.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.order.OrderCommand.AddOrderLineCommand;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class OrderCommandFixture {

    public static class OrderLine {

        public static OrderLineBuilder builder() {
            return new OrderLineBuilder();
        }

        public static class OrderLineBuilder {

            private InstancioApi<AddOrderLineCommand> api;

            public OrderLineBuilder() {
                this.api = Instancio.of(AddOrderLineCommand.class);
            }

            public OrderLineBuilder withProductId(Long productId) {
                this.api = this.api.set(field(AddOrderLineCommand::productId), productId);
                return this;
            }

            public OrderLineBuilder withPrice(Long price) {
                this.api = this.api.set(field(AddOrderLineCommand::price), price);
                return this;
            }

            public OrderLineBuilder withQuantity(Long quantity) {
                this.api = this.api.set(field(AddOrderLineCommand::quantity), quantity);
                return this;
            }

            public AddOrderLineCommand build() {
                return this.api.create();
            }
        }
    }
}
