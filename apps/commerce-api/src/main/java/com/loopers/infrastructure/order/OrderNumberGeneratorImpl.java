package com.loopers.infrastructure.order;

import com.loopers.domain.order.OrderNumberGenerator;
import java.util.UUID;
import org.springframework.stereotype.Component;

@Component
class OrderNumberGeneratorImpl implements OrderNumberGenerator {

    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
