package com.loopers.interfaces.api.user;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.instancio.Select.field;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.loopers.interfaces.api.ApiResponse;
import com.loopers.testcontainers.MySqlTestContainersConfig;
import com.loopers.utils.DatabaseCleanUp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(MySqlTestContainersConfig.class)
class UserV1ApiE2Test {

    private static final String ENDPOINT = "/api/v1/users";

    private final TestRestTemplate testRestTemplate;
    private final DatabaseCleanUp databaseCleanUp;

    @Autowired
    public UserV1ApiE2Test(
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
            var requestBody = Instancio.of(UserV1Dto.SignUpRequest.class)
                .generate(field(UserV1Dto.SignUpRequest::userId), gen -> gen.string()
                    .length(5, 10)
                    .alphaNumeric()
                )
                .generate(field(UserV1Dto.SignUpRequest::email), gen -> gen.net().email())
                .generate(field(UserV1Dto.SignUpRequest::birth), gen -> gen.temporal()
                    .localDate()
                    .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
                    .as(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                )
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
                () -> assertThat(userData.birth()).isEqualTo(requestBody.birth())
            );
        }
    }
}
