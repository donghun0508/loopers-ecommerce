package com.loopers.interfaces.api;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Slice;

@Builder
@Getter
public class NoCountPageResponse<T> {

    boolean isLast;
    List<T> content;

    public static <T> NoCountPageResponse<T> of(Slice<T> slice) {  // 오직 Slice. 카운트 쿼리 최적화
        return NoCountPageResponse.<T>builder()
            .isLast(slice.isLast())
            .content(slice.getContent())
            .build();
    }
}
