package com.loopers.application.heart.validate;

import com.loopers.domain.catalog.Product;
import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.heart.TargetType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static com.loopers.domain.heart.TargetType.PRODUCT;

@Slf4j
@RequiredArgsConstructor
@Component
class ProductTargetPostProcess implements TargetPostProcess {

    private final ProductService productService;

    @Override
    public boolean supports(TargetType targetType) {
        return PRODUCT.equals(targetType);
    }

    @Override
    public void process(Long targetId) {
        Product product = productService.findById(targetId);
        product.updateHeartCount();
    }
}
