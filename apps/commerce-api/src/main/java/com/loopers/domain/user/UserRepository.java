package com.loopers.domain.user;

import org.springframework.data.repository.Repository;

import java.util.Optional;


public interface UserRepository extends Repository<User, Long> {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByAccountId(AccountId accountId);

    boolean existsByAccountIdAndEmail(AccountId accountId, Email email);
}
