package com.loopers.infrastructure.client;


public record PaymentRequest(
    String orderId,
    CardTypeRequest cardType,
    String cardNo,
    Long amount,
    String callbackUrl
) {
    enum CardTypeRequest {
        SAMSUNG,
        KB,
        HYUNDAI,
        ;
    }
}
