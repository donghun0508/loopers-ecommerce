package com.loopers.config;

import com.loopers.domain.product.StockManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DomainConfig {

    @Bean
    public StockManager stockManager() {
        return new StockManager();
    }

}
