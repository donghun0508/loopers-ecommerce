package com.loopers.domain.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import com.loopers.domain.user.fixture.UserCommandFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockitoSpyBean
    private UserRepository userRepository;

    @DisplayName("회원 서비스에서,")
    @Nested
    class Create {

        @DisplayName("회원 도메인 객체 생성 후 User 저장이 수행된다. ( spy 검증 )")
        @Test
        void returnsUser_whenCreateUser() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().build();
            User expectedUser = User.of(command);
            doReturn(expectedUser).when(userRepository).save(any(User.class));

            // act
            User savedUser = userService.create(command);

            // assert
            assertThat(savedUser).isNotNull();
            assertThat(savedUser.getId()).isEqualTo(expectedUser.getId());
        }
    }
}
