package com.loopers.domain.order;

import static java.util.Objects.requireNonNull;

import lombok.Builder;

public class OrderCommand {

    @Builder
    public record Create(Long userId) {
        public Create {
            requireNonNull(userId, "사용자 Id가 null입니다.");
        }
    }

    @Builder
    public record AddOrderLine(Long productId, Long price, Long quantity) {
        public AddOrderLine {
            requireNonNull(productId, "상품 Id가 null입니다.");
            requireNonNull(price, "구매할 상품 가격이 null입니다.");
            requireNonNull(quantity, "구매할 상품 수량이 null입니다.");
        }
    }
}
