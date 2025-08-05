package com.loopers.domain.heart;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

@Getter(AccessLevel.PACKAGE)
@Entity
@Table(
        name = "heart"
        , uniqueConstraints = {
        @UniqueConstraint(
                name = "UK_HEART_USER_TARGET_TYPE",
                columnNames = {"user_id", "target_id", "target_type"}
        )
    }
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart extends BaseEntity {

    @Column(
            name = "user_id",
            nullable = false,
            updatable = false
    )
    private Long userId;

    @AttributeOverrides({
            @AttributeOverride(name = "targetId", column = @Column(name = "target_id", nullable = false, updatable = false)),
            @AttributeOverride(name = "targetType", column = @Column(name = "target_type", nullable = false, updatable = false))
    })
    private Target target;

    public static Heart from(HeartCreateCommand command) {
        requireNonNull(command, "좋아요 생성 명령은 null일 수 없습니다");

        Heart heart = new Heart();
        heart.userId = command.userId();
        heart.target = command.target();

        return heart;
    }
}
