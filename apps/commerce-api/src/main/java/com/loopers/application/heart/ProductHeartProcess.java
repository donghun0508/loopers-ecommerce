package com.loopers.application.heart;

import com.loopers.domain.catalog.entity.Product;
import com.loopers.domain.catalog.service.ProductService;
import com.loopers.domain.heart.TargetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.loopers.domain.heart.TargetType.PRODUCT;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductHeartProcess implements HeartProcess {

    private final ProductService productService;

    @Override
    public boolean supports(TargetType targetType) {
        return PRODUCT.equals(targetType);
    }

    @Override
    public void addHeart(Long targetId) {
        Product product = productService.findByIdWithLock(targetId);
        product.heart();
    }

    @Override
    public void unHeart(Long targetId) {
        Product product = productService.findByIdWithLock(targetId);
        product.unHeart();
    }
}
