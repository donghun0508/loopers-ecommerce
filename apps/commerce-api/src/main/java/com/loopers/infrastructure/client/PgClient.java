package com.loopers.infrastructure.client;

import com.loopers.config.PaymentConfig;
import com.loopers.interfaces.api.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
    name = "pgClient",
    url = "${external.payment.providers.simulator.base-url}",
    configuration = PaymentConfig.class
)
public interface PgClient {

    @PostMapping("/api/v1/payments")
    ApiResponse<PaymentResponse> requestPayment(@RequestBody PaymentRequest request);
}
