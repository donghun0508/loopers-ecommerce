package com.loopers.domain.heart;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HeartQueryService {

    private final HeartQueryRepository heartQueryRepository;

    public List<Heart> findAllByProductId(List<Long> productIds) {
        return null;
    }

    public List<Heart> getHeartList(Long userId, Pageable pageable) {
        return null;
    }

    public boolean existsByUserIdAndTarget(Long userId, Target target) {
        return heartQueryRepository.existsByUserIdAndTarget(userId, target);
    }
}
