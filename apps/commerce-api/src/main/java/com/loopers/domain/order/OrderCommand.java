package com.loopers.domain.order;


import static com.loopers.utils.ValidationUtils.requireNonNull;

import lombok.Builder;

public class OrderCommand {

    @Builder
    public record Create(Long userId) {

        public Create {
            requireNonNull(userId, "사용자 Id가 null입니다.");
        }
    }
}
