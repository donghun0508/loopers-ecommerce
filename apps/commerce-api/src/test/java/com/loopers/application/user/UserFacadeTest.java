package com.loopers.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserService;
import com.loopers.domain.user.fixture.UserCommandFixture;
import com.loopers.support.error.user.UserAlreadyExistsException;
import com.loopers.support.error.user.UserErrorType;
import com.loopers.testcontainers.MySqlTestContainersConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(MySqlTestContainersConfig.class)
public class UserFacadeTest {

    @Autowired
    private UserFacade userFacade;

    @MockitoSpyBean
    private UserService userService;

    @DisplayName("회원 가입 시,")
    @Nested
    class SignUp {

        @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
        @Test
        void throwsException_whenUserIdDuplicate() {
            // arrange
            String duplicateUserId = "testUser";
            UserCommand.Create command = UserCommandFixture.Create.complete()
                .userId(duplicateUserId)
                .build();
            userFacade.signUp(command);

            // act
            UserAlreadyExistsException exception =
                assertThrows(UserAlreadyExistsException.class, () -> userFacade.signUp(command));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(UserErrorType.USER_ALREADY_EXISTS);
        }

        @DisplayName("정상적인 회원 데이터인 경우, 회원가입에 성공한다.")
        @Test
        void  doesNotThrow_whenValidUserData() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().build();

            // act & assert
            assertDoesNotThrow(() -> userFacade.signUp(command));
            verify(userService).findByUserId(command.userId());
            verify(userService).create(command);
        }
    }
}
