package com.loopers.domain.product;

import java.util.Objects;
import lombok.Builder;

public class ProductCommand {

    @Builder
    public record DecreaseStock(Stock quantity) {

        public DecreaseStock {
            Objects.requireNonNull(quantity, "Stock cannot be null");
        }
    }
}
