package com.loopers.application.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import com.loopers.domain.user.UserCommand;
import com.loopers.domain.user.UserService;
import com.loopers.domain.user.fixture.UserCommandFixture;
import com.loopers.support.error.ErrorType;
import com.loopers.support.error.user.UserAlreadyExistsException;
import com.loopers.support.error.user.UserNotFoundException;
import com.loopers.testcontainers.MySqlTestContainersConfig;
import com.loopers.utils.DatabaseCleanUp;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
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
public class UserFacadeIntegrationTest {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @MockitoSpyBean
    private UserService userService;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("회원 가입 시, ")
    @Nested
    class SignUp {

        @DisplayName("이미 가입된 ID 로 회원가입 시도 시, 실패한다.")
        @Test
        void throwsException_whenUserIdDuplicate() {
            // arrange
            String duplicateUserId = "testUser";
            UserCommand.Create command = UserCommandFixture.Create.complete()
                .set(field(UserCommand.Create::userId), duplicateUserId)
                .create();
            userFacade.signUp(command);

            // act
            UserAlreadyExistsException exception =
                assertThrows(UserAlreadyExistsException.class, () -> userFacade.signUp(command));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.CONFLICT);
        }

        @DisplayName("정상적인 회원 데이터인 경우, 회원가입에 성공한다.")
        @Test
        void doesNotThrow_whenValidUserData() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();

            // act & assert
            assertDoesNotThrow(() -> userFacade.signUp(command));
            verify(userService).findByUserId(command.userId());
            verify(userService).create(command);
        }
    }

    @DisplayName("회원 조회 시, ")
    @Nested
    class Get {

        @DisplayName("해당 ID 의 회원이 존재할 경우, 회원 정보가 반환된다.")
        @Test
        void returnsUserInfo_whenValidIdIsProvided() {
            // arrange
            UserCommand.Create command = UserCommandFixture.Create.complete().create();
            UserInfo savedUserInfo = userFacade.signUp(command);

            // act
            UserInfo findUserInfo = userFacade.getUser(savedUserInfo.id());

            // assert
            assertThat(findUserInfo).isNotNull();
            assertThat(findUserInfo.id()).isEqualTo(savedUserInfo.id());
            assertThat(findUserInfo.userId()).isEqualTo(savedUserInfo.userId());
            assertThat(findUserInfo.birth()).isEqualTo(savedUserInfo.birth());
            assertThat(findUserInfo.gender()).isEqualTo(savedUserInfo.gender());
        }

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, null 이 반환된다.")
        @Test
        void returnsNull_whenUserNotFound() {
            // arrange
            Long randomId = Instancio.create(Long.class);

            // act
            UserNotFoundException exception =
                assertThrows(UserNotFoundException.class, () -> userFacade.getUser(randomId));

            // assert
            assertThat(exception.getErrorCode()).isEqualTo(ErrorType.NOT_FOUND);
        }
    }
}
