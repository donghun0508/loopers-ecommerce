package com.loopers.domain.point;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PointService {

    private final PointRepository pointRepository;

    @Transactional
    public Point create(PointCommand.Create command) {
        Point point = Point.of(command);
        return pointRepository.save(point);
    }

    @Transactional(readOnly = true)
    public Optional<Point> findByUserId(Long userId) {
        return pointRepository.findByUserId(userId);
    }
}
