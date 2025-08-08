package com.loopers.domain.heart;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface HeartRepository extends Repository<Heart, Long> {
    Heart save(Heart heart);

    void deleteByUserIdAndTarget(Long userId, Target target);

    Optional<Heart> findByUserIdAndTarget(Long userId, Target target);

    long count();
}
