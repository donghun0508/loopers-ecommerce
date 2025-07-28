package com.loopers.domain.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.order.OrderCommand.OrderLine.AddCommand;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class OrderCommandFixture {

    public static class OrderLine {

        public static OrderLineBuilder builder() {
            return new OrderLineBuilder();
        }

        public static class OrderLineBuilder {

            private InstancioApi<AddCommand> api;

            public OrderLineBuilder() {
                this.api = Instancio.of(AddCommand.class);
            }

            public OrderLineBuilder withProductId(Long productId) {
                this.api = this.api.set(field(AddCommand::productId), productId);
                return this;
            }

            public OrderLineBuilder withPrice(Long price) {
                this.api = this.api.set(field(AddCommand::price), price);
                return this;
            }

            public OrderLineBuilder withQuantity(Long quantity) {
                this.api = this.api.set(field(AddCommand::quantity), quantity);
                return this;
            }

            public AddCommand build() {
                return this.api.create();
            }
        }
    }
}
