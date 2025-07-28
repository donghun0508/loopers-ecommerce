package com.loopers.domain.order;

import static java.util.Objects.requireNonNull;

import lombok.Builder;

public class OrderCommand {

    @Builder
    public record AddOrderLineCommand(Long productId, Long price, Long quantity) {
        public AddOrderLineCommand {
            requireNonNull(productId, "상품 Id가 null입니다.");
            requireNonNull(price, "구매할 상품 가격이 null입니다.");
            requireNonNull(quantity, "구매할 상품 수량이 null입니다.");
        }
    }

}
