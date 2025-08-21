package com.loopers.infrastructure.client;

public record PaymentResponse(
    String transactionKey,
    TransactionStatusResponse status,
    String reason
) {
    enum TransactionStatusResponse {
        PENDING,
        SUCCESS,
        FAILED,
        ;
    }

    public boolean isSuccess() {
        return status == TransactionStatusResponse.SUCCESS;
    }
}
