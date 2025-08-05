package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.loopers.support.error.ErrorType.NOT_FOUND;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(UserCreateCommand command) {
        try {
            if (userRepository.existsByAccountIdAndEmail(command.accountId(), command.email())) {
                log.warn("동일한 회원의 존재합니다. : {}", command);
                throw new CoreException(ErrorType.CONFLICT);
            }

            User user = User.from(command);
            return userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            log.warn("회원 가입 중 데이터 무결성 위반 발생: {}", e.getMessage(), e);
            throw new CoreException(ErrorType.CONFLICT, "이미 존재하는 계정입니다.", e);
        }
    }

    @Transactional(readOnly = true)
    public User findByAccountId(AccountId accountId) {
        return userRepository.findByAccountId(accountId).orElseThrow(() -> new CoreException(NOT_FOUND));
    }
}
