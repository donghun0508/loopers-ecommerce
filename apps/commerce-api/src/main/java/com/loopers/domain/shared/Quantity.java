package com.loopers.domain.shared;

import jakarta.persistence.Embeddable;

@Embeddable
public record Quantity(Long count) {

}
