package com.loopers.domain.product;

import java.util.Objects;
import lombok.Builder;

public class ProductCommand {

    @Builder
    public record DecreaseStockCommand(Stock quantity) {

        public DecreaseStockCommand {
            Objects.requireNonNull(quantity, "Stock cannot be null");
        }
    }
}
