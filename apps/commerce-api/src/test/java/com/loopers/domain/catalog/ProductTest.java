package com.loopers.domain.catalog;

import com.loopers.config.annotations.UnitTest;
import com.loopers.fixture.ProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;

import static org.junit.jupiter.api.Assertions.*;

@UnitTest
class ProductTest {

    @DisplayName("재고 감소 시, 재고 파라미터가 null이면 예외를 발생시킨다.")
    @ParameterizedTest
    @NullSource
    void throwsException_whenStockIsNull(Stock invalidStock) {
        Product product = ProductFixture.builder().build();
        assertThrows(IllegalArgumentException.class, () -> product.decreaseStock(invalidStock));
    }

}
