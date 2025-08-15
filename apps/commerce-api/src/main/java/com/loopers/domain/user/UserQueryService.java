package com.loopers.domain.user;

import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserQueryService {

    private final UserQueryRepository userQueryRepository;

    public User getUser(AccountId accountId) {
        return userQueryRepository.findByAccountId(accountId)
            .orElseThrow(() -> new CoreException(ErrorType.NOT_FOUND, "사용자를 찾을 수 없습니다. AccountId: " + accountId));
    }
}
