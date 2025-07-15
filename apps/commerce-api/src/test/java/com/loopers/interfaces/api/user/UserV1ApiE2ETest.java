package com.loopers.interfaces.api.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.fixture.UserV1DtoFixture;
import com.loopers.testcontainers.MySqlTestContainersConfig;
import com.loopers.utils.DatabaseCleanUp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(MySqlTestContainersConfig.class)
class UserV1ApiE2ETest {

    private static final String ENDPOINT = "/api/v1/users";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserV1ApiE2ETest(
        TestRestTemplate testRestTemplate,
        DatabaseCleanUp databaseCleanUp
    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("POST /api/v1/users")
    @Nested
    class Post {

        @DisplayName("회원 가입이 성공할 경우, 생성된 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsUserResponse_whenValidUserData() {
            // arrange
            var requestBody = UserV1DtoFixture.SignUpRequest.CACHE_FIXTURE;

            // act
            var response = testRestTemplate.exchange(
                ENDPOINT,
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>>() {
                }
            );

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().data()).isNotNull()
            );

            var userData = response.getBody().data();
            assertAll(
                () -> assertThat(userData.id()).isNotNull(),
                () -> assertThat(userData.userId()).isEqualTo(requestBody.userId()),
                () -> assertThat(userData.email()).isEqualTo(requestBody.email()),
                () -> assertThat(userData.birth()).isEqualTo(requestBody.birth()),
                () -> assertThat(userData.gender().name()).isEqualTo(requestBody.gender().name())
            );
        }

        @DisplayName("회원 가입 시 성별이 없을 경우, 400 Bad Request를 응답한다.")
        @Test
        void returnsBadRequest_whenGenderIsMissing() {
            var requestBody = UserV1DtoFixture.SignUpRequest.complete()
                .ignore(field(UserV1Dto.SignUpRequest::gender))
                .create();

            // act
            var response = testRestTemplate.exchange(
                ENDPOINT,
                HttpMethod.POST,
                new HttpEntity<>(requestBody),
                new ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>>() {
                }
            );

            // assert
            assertAll(() -> assertTrue(response.getStatusCode().is4xxClientError()));
        }
    }
}
