package com.loopers.interfaces.api.payment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/payments")
public class PaymentV1Controller {

    @PostMapping("/callback")
    public void callback(String message) {
        log.info("Payment callback received: {}", message);
    }

}
