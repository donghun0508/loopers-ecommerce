package com.loopers.domain.user;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface UserRepository extends Repository<User, Long> {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByAccountId(AccountId accountId);

    boolean existsByAccountIdAndEmail(AccountId accountId, Email email);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM User u WHERE u.accountId = :accountId")
    Optional<User> findByAccountIdWithLock(@Param("accountId") AccountId accountId);

    List<User> findAllByIdIn(Collection<Long> ids);
}
