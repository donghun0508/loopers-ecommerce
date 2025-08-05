package com.loopers.fixture;

import com.loopers.domain.order.OrderCreateCommand;

import com.loopers.domain.order.OrderCreateCommand.OrderItem;
import com.loopers.domain.order.Quantity;
import com.loopers.domain.shared.Money;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

import java.util.List;

import static org.instancio.Select.field;

public class OrderCreateCommandFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final InstancioApi<OrderCreateCommand> api;
        static final Integer MIN_TEST_SIZE = 1;
        static final Integer MAX_TEST_SIZE = 10;

        public Builder() {
            this.api = Instancio.of(OrderCreateCommand.class)
                    .set(field(OrderCreateCommand::items),
                            Instancio.ofList(OrderItem.class)
                                    .generate(Select.root(), gen -> gen.collection()
                                            .minSize(MIN_TEST_SIZE)
                                            .maxSize(MAX_TEST_SIZE))
                                    .generate(field(OrderItem::productId), gen -> gen.longs().min(1L).max(1000L))
                                    .generate(field(OrderItem::price), gen -> gen.longs().min(1000L).max(100000L)
                                            .as(Money::of))
                                    .generate(field(OrderItem::quantity), gen -> gen.longs().min(1L).max(10L)
                                            .as(Quantity::of))
                                    .create()
                    )
            ;
        }

        public Builder buyerId(Long buyerId) {
            this.api.set(field(OrderCreateCommand::buyerId), buyerId);
            return this;
        }

        public Builder orderItems(int size) {
            List<OrderItem> orderItems = Instancio.ofList(OrderItem.class)
                    .size(size)
                    .generate(field(OrderItem::productId), gen -> gen.longs().min(1L).max(1000L))
                    .generate(field(OrderItem::price), gen -> gen.longs().min(1000L).max(100000L))
                    .generate(field(OrderItem::quantity), gen -> gen.longs().min(1L).max(10L))
                    .create();

            this.api.set(field(OrderCreateCommand::items), orderItems);
            return this;
        }

        public OrderCreateCommand build() {
            return api.create();
        }
    }
}
