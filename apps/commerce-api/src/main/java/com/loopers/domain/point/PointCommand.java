package com.loopers.domain.point;

import lombok.Builder;

public class PointCommand {

    @Builder
    public record Create(Long userId) {

    }

}
