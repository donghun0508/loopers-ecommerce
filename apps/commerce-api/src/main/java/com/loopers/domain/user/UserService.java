package com.loopers.domain.user;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User create(UserCommand.Create command) {
        User user = User.of(command);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUserId(String userId) {
        return userRepository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}
