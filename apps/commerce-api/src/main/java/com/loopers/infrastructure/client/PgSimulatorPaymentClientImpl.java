package com.loopers.infrastructure.client;

import com.loopers.config.PaymentConfig.SimulatorProviderConfig.OperationConfig.OperationPaymentRequestConfig;
import com.loopers.domain.payment.PaymentClient;
import com.loopers.domain.payment.SupportPg.PgSimulator;
import com.loopers.infrastructure.client.PaymentRequest.CardTypeRequest;
import com.loopers.interfaces.api.ApiResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@PgSimulator
class PgSimulatorPaymentClientImpl implements PaymentClient {

    private final PgClient pgClient;

    @Override
    @CircuitBreaker(name = OperationPaymentRequestConfig.Constants.CIRCUIT_PAYMENT, fallbackMethod = "fallbackPayment")
    @Retry(name = OperationPaymentRequestConfig.Constants.RETRY_PAYMENT)
    public PaymentClientResponse requestPayment(PaymentClientRequest paymentClientRequest) {

        PaymentRequest request = new PaymentRequest(
            paymentClientRequest.orderId(),
            CardTypeRequest.valueOf(paymentClientRequest.cardType().name()),
            paymentClientRequest.cardNo(),
            paymentClientRequest.amount(),
            paymentClientRequest.callbackUrl()
        );

        ApiResponse<PaymentResponse> response = pgClient.requestPayment(request);

        return null;
    }

    public PaymentClientResponse fallbackPayment(PaymentClientRequest request, Exception ex) {
        log.error("결제 요청 실패, fallback 실행. 요청: {}", request, ex);
        return null;
    }
}
