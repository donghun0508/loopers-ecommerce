package com.loopers.domain.payment;

public interface PaymentClient {

    PaymentClientResponse requestPayment(PaymentClientRequest request);

    record PaymentClientRequest(
        String orderId,
        CardType cardType,
        String cardNo,
        Long amount,
        String callbackUrl
    ) {

    }

    record PaymentClientResponse(
        String transactionKey,
        TransactionStatusResponse status,
        String reason

    ) {

        public boolean isPending() {
            return this.status == TransactionStatusResponse.PENDING;
        }

        public enum TransactionStatusResponse {
            PENDING,
            SUCCESS,
            FAILED,
        }
    }
}
