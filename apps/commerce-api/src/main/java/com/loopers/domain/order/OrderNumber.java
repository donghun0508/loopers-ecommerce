package com.loopers.domain.order;

import jakarta.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public record OrderNumber(String number) {

    private static final String PREFIX = "29CART-";

    public static OrderNumber generate() {
        String uuid = UUID.randomUUID()
            .toString()
            .replace("-", "")
            .substring(0, 12)
            .toUpperCase();
        return new OrderNumber(PREFIX + uuid);
    }
}
