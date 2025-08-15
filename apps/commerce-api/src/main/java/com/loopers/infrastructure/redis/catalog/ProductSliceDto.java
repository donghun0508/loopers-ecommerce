package com.loopers.infrastructure.redis.catalog;

import com.loopers.domain.catalog.ProductRead;
import com.loopers.domain.catalog.entity.Product;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Slice;

@Getter
@Setter
public class ProductSliceDto {

    private List<ProductRead> content;
    private boolean hasNext;
    private int pageNumber;
    private int pageSize;

    public ProductSliceDto() {
    }

    public ProductSliceDto(Slice<Product> slice) {
        this.content = slice.getContent().stream()
            .map(ProductRead::new)
            .toList();
        this.hasNext = slice.hasNext();
        this.pageNumber = slice.getPageable().getPageNumber();
        this.pageSize = slice.getPageable().getPageSize();
    }


}
