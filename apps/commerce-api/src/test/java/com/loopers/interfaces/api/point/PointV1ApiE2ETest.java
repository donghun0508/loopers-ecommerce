package com.loopers.interfaces.api.point;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.loopers.domain.user.UserRepository;
import com.loopers.interfaces.api.ApiResponse;
import com.loopers.interfaces.api.user.UserV1Dto;
import com.loopers.interfaces.api.user.UserV1Dto.UserResponse;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(MySqlTestContainersConfig.class)
class PointV1ApiE2ETest {

    private static final String ENDPOINT = "/api/v1/points";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;
    private final UserRepository userRepository;

    @Autowired
    public PointV1ApiE2ETest(
        TestRestTemplate testRestTemplate,
        DatabaseCleanUp databaseCleanUp,
        UserRepository userRepository
    ) {
        this.testRestTemplate = testRestTemplate;
        this.databaseCleanUp = databaseCleanUp;
        this.userRepository = userRepository;
    }

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("GET /api/v1/points")
    @Nested
    class Get {

        @DisplayName("존재하지 않는 ID 로 조회할 경우, `404 Not Found` 응답을 반환한다.")
        @Test
        void returns404NotFound_whenUserDoesNotExist() {
            // arrange
            String userId = "12345";
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", userId);

            // act
            ParameterizedTypeReference<ApiResponse<UserResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            var response =
                testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is4xxClientError()),
                () -> assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND)
            );
        }

        @DisplayName("해당 ID 의 회원이 존재할 경우, 보유 포인트를 응답으로 반환한다.")
        @Test
        void returnsUserPointResponse_whenUserExist() {
            // arrange
            UserV1Dto.SignUpRequest request = UserV1DtoFixture.SignUpRequest.complete().create();
            String userId = testRestTemplate.exchange("/api/v1/users", HttpMethod.POST, new HttpEntity<>(request), new ParameterizedTypeReference<ApiResponse<UserV1Dto.UserResponse>>() {
                })
                .getBody()
                .data()
                .userId();
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-USER-ID", String.valueOf(userId));

            // act
            ParameterizedTypeReference<ApiResponse<PointV1Dto.PointResponse>> responseType = new ParameterizedTypeReference<>() {
            };
            var response =
                testRestTemplate.exchange(ENDPOINT, HttpMethod.GET, new HttpEntity<>(headers), responseType);

            // assert
            assertAll(
                () -> assertTrue(response.getStatusCode().is2xxSuccessful()),
                () -> assertThat(response.getBody().data().point()).isZero()
            );
        }
    }
}
