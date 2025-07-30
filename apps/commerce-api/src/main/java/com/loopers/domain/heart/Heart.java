package com.loopers.domain.heart;

import static com.loopers.utils.ValidationUtils.requireNonNull;

import com.loopers.domain.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Heart extends BaseEntity {

    @Column(
        name = "user_id",
        nullable = false,
        updatable = false
    )
    private Long userId;

    @Column(
        name = "target_id",
        nullable = false,
        updatable = false
    )
    private Long targetId;

    @Column(
        name = "target_type",
        nullable = false,
        updatable = false
    )
    @Enumerated(EnumType.STRING)
    private TargetType targetType;

    public static Heart create(HeartCommand.Create command) {
        requireNonNull(command, "좋아요 명령이 null일 수 없습니다.");

        Heart heart = new Heart();
        heart.userId = command.userId();
        heart.targetId = command.targetId();
        heart.targetType = command.targetType();

        return heart;
    }
}
