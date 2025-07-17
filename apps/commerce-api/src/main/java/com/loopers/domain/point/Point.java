package com.loopers.domain.point;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;

@Getter
@Entity
@Table(name = "point")
public class Point extends BaseEntity {

    @Column(
        name = "user_id",
        updatable = false,
        nullable = false,
        unique = true
    )
    private Long userId;

    @Column(
        name = "amount",
        nullable = false
    )
    private Long amount;

    protected Point() {

    }

    private Point(Long userId, Long amount) {
        this.userId = userId;
        this.amount = amount;
    }

    public static Point of(PointCommand.Create command) {
        validate(command);
        return new Point(command.userId(), 0L);
    }

    private static void validate(PointCommand.Create command) {
        Validator.userId(command.userId());
    }

    private static class Validator {

        public static void userId(Long userId) {
            if(userId == null) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }
    }
}
