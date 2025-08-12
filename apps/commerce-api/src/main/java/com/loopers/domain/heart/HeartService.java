package com.loopers.domain.heart;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeartService {

    private final HeartRepository heartRepository;

    @Transactional
    public Heart create(HeartCreateCommand command) {
        try {
            Optional<Heart> existingHeart = heartRepository.findByUserIdAndTarget(command.userId(), command.target());
            if (existingHeart.isPresent()) {
                throw new DataIntegrityViolationException("이미 좋아요를 누른 대상입니다.");
            }
            return heartRepository.save(Heart.from(command));
        } catch (DataIntegrityViolationException e) {
            throw new CoreException(ErrorType.CONFLICT, "이미 좋아요를 눌렀습니다.", e);
        }
    }

    @Transactional
    public void delete(Long userId, Target target) {
        heartRepository.deleteByUserIdAndTarget(userId, target);
    }
}
