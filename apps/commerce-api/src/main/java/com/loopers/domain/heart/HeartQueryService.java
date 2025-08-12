package com.loopers.domain.heart;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class HeartQueryService {
    public List<Heart> findAllByProductId(List<Long> productIds) {
        return null;
    }

    public List<Heart> getHeartList(Long userId, Pageable pageable) {
        return null;
    }
}
