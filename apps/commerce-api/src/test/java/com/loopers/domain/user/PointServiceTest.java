package com.loopers.domain.user;


import static org.assertj.core.api.Assertions.assertThat;

import com.loopers.domain.point.Point;
import com.loopers.domain.point.PointCommand;
import com.loopers.domain.point.PointRepository;
import com.loopers.domain.point.PointService;
import com.loopers.domain.user.fixture.UserCommandFixture;
import com.loopers.utils.DatabaseCleanUp;
import java.util.Optional;
import org.instancio.Instancio;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseCleanUp databaseCleanUp;

    @AfterEach
    void tearDown() {
        databaseCleanUp.truncateAllTables();
    }

    @DisplayName("포인트 조회 시,")
    @Nested
    class Read {

        @DisplayName("해당 ID 의 회원이 존재하지 않을 경우, Null을 반환한다.")
        @Test
        void returnNull_whenInValidUserIdIsProvided() {
            // arrange
            Long randomUserId = Instancio.create(Long.class);

            // act
            Optional<Point> optionalPoint = pointService.findByUserId(randomUserId);

            // assert
            assertThat(optionalPoint).isNotPresent();
        }

        @DisplayName("해당 ID 의 회원이 존재할 경우, 포인트 객체를 반환한다.")
        @Test
        void returnsPoint_whenValidUserIdIsProvided() {
            // arrange
            UserCommand.Create userCreateCommand = UserCommandFixture.Create.complete().create();
            User user = User.of(userCreateCommand);
            User savedUser = userRepository.save(user);

            Long userPk = savedUser.getId();
            PointCommand.Create pointCreateCommand = PointCommand.Create.builder()
                .userId(userPk)
                .build();
            Point point = Point.of(pointCreateCommand);
            Point savedPoint = pointRepository.save(point);

            // act
            Optional<Point> optionalPoint = pointService.findByUserId(userPk);

            // assert
            assertThat(optionalPoint).isPresent();
            Point actualPoint = optionalPoint.get();
            assertThat(actualPoint.getId()).isEqualTo(savedPoint.getId());
            assertThat(actualPoint.getUserId()).isEqualTo(userPk);
            assertThat(actualPoint.getAmount()).isZero();
        }

    }
}
