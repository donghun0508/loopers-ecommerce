package com.loopers.application.heart.validate;

import com.loopers.domain.catalog.ProductService;
import com.loopers.domain.heart.TargetType;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.loopers.domain.heart.TargetType.PRODUCT;

@RequiredArgsConstructor
@Component
class ProductTargetValidate implements TargetValidate {

    private final ProductService productService;

    @Override
    public boolean supports(TargetType targetType) {
        return PRODUCT.equals(targetType);
    }

    @Override
    public void validTargetId(Long targetId) {
        if (!productService.existsById(targetId)) {
            throw new CoreException(ErrorType.NOT_FOUND);
        }
    }
}
