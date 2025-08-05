package com.loopers.fixture;

import com.loopers.domain.shared.Money;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class MoneyFixture {

    public static MoneyBuilder builder() {
        return new MoneyBuilder();
    }

    public static class MoneyBuilder {

        static final Long MIN_TEST_AMOUNT = 1L;
        static final Long MAX_TEST_AMOUNT = 1_000_000L;

        private final InstancioApi<Money> api;

        MoneyBuilder() {
            this.api = Instancio.of(Money.class)
                    .generate(field(Money::value), gen -> gen.longs().range(MIN_TEST_AMOUNT, MAX_TEST_AMOUNT));
        }

        public MoneyBuilder amount(long amount) {
            this.api.set(field(Money::value), amount);
            return this;
        }

        public Money build() {
            return this.api.create();
        }
    }
}
