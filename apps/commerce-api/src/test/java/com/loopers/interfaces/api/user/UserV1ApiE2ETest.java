package com.loopers.interfaces.api.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.example.ExampleV1Dto;
import com.loopers.interfaces.api.user.UserV1Dto.UserResponse;
import com.loopers.interfaces.api.user.fixture.UserV1DtoFixture;
import com.loopers.testcontainers.MySqlTestContainersConfig;
import com.loopers.utils.DatabaseCleanUp;
import java.util.function.Function;
import org.assertj.core.api.Assertions;
import org.instancio.Instancio;
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
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(MySqlTestContainersConfig.class)
class UserV1ApiE2ETest {

    private static final String ENDPOINT = "/api/v1/users";
    private static final Function<Long, String> ENDPOINT_GET = id -> "/api/v1/users/" + id;

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
            UserV1Dto.SignUpRequest request = UserV1DtoFixture.SignUpRequest.CACHE_FIXTURE;

            // act
            var response = signUpRequest(request);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().data()).isNotNull()
            );

            assertUserDataEquals(request, response.getBody().data());
        }

        @DisplayName("회원 가입 시 성별이 없을 경우, 400 Bad Request를 응답한다.")
        @Test
        void returnsBadRequest_whenGenderIsMissing() {
            var request = UserV1DtoFixture.SignUpRequest.complete()
                .ignore(field(UserV1Dto.SignUpRequest::gender))
                .create();

            // act
            var response = signUpRequest(request);

            // assert
            assertAll(() -> assertTrue(response.getStatusCode().is4xxClientError()));
        }
    }

    @DisplayName("GET /api/v1/users/{id}")
    @Nested
    class GET {

        @DisplayName("존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.")
        @Test
        void returns404NotFound_whenUserDoesNotExist() {
            // arrange
            Long randomId = Instancio.create(Long.class);
            String requestUrl = ENDPOINT_GET.apply(randomId);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() { };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError())
            );
        }

        @DisplayName("내 정보 조회에 성공할 경우, 해당하는 유저 정보를 응답으로 반환한다.")
        @Test
        void returnsUserResponse_whenValidIdIsProvided() {
            // arrange
            UserV1Dto.SignUpRequest request = UserV1DtoFixture.SignUpRequest.complete().create();
            Long id = signUpRequest(request).getBody().data().id();
            String requestUrl = ENDPOINT_GET.apply(id);

            // act
            ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>> responseType = new ParameterizedTypeReference<>() { };
            ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> response =
                testRestTemplate.exchange(requestUrl, HttpMethod.GET, new HttpEntity<>(null), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody()).isNotNull(),
                () -> assertThat(response.getBody().data()).isNotNull()
            );

            assertUserDataEquals(request, response.getBody().data());
        }
    }

    private ResponseEntity<ApiResponse<UserV1Dto.UserResponse>> signUpRequest(Object requestBody) {
        return testRestTemplate.exchange(
            ENDPOINT,
            HttpMethod.POST,
            new HttpEntity<>(requestBody),
            new ParameterizedTypeReference<>() {
            }
        );
    }

    private void assertUserDataEquals(UserV1Dto.SignUpRequest expected, UserResponse actual) {
        assertAll(
            () -> assertThat(actual.id()).isNotNull(),
            () -> assertThat(actual.userId()).isEqualTo(expected.userId()),
            () -> assertThat(actual.email()).isEqualTo(expected.email()),
            () -> assertThat(actual.birth()).isEqualTo(expected.birth()),
            () -> assertThat(actual.gender().name()).isEqualTo(expected.gender().name())
        );
    }
}
