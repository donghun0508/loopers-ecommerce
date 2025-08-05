package com.loopers.domain.heart;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class HeartService {

    private final HeartRepository heartRepository;

    @Transactional
    public Heart create(HeartCreateCommand command) {
        try {
            return heartRepository.findByUserIdAndTarget(command.userId(), command.target())
                    .map(heart -> {
                        log.warn("이미 좋아요를 누른 대상입니다. accountId: {}, target: {}", command.userId(), command.target());
                        return heart;
                    })
                    .orElseGet(() -> heartRepository.save(Heart.from(command)));
        } catch (DataIntegrityViolationException e) {
            log.warn("이미 좋아요를 눌렀습니다. {}", command);
            throw new CoreException(ErrorType.CONFLICT, "이미 좋아요를 눌렀습니다.", e);
        }
    }

    @Transactional
    public void delete(Long userId, Target target) {
        heartRepository.deleteByUserIdAndTarget(userId, target);
    }

}
