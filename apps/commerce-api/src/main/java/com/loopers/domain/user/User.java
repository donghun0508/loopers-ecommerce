package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.shared.Money;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import static com.loopers.domain.shared.Preconditions.requireNonNull;

@Getter
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_USER_ACCOUNT_ID", columnNames = {"account_id"}),
                @UniqueConstraint(name = "UK_USER_EMAIL", columnNames = {"email"})
        }
)
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseEntity {

    @NaturalId
    @AttributeOverride(
            name = "value",
            column = @Column(
                    name = "account_id",
                    nullable = false,
                    unique = true
            )
    )
    private AccountId accountId;

    @AttributeOverride(
            name = "address",
            column = @Column(
                    name = "email",
                    nullable = false,
                    unique = true
            )
    )
    private Email email;

    @AttributeOverride(
            name = "day",
            column = @Column(
                    name = "birth",
                    nullable = false
            )
    )
    private Birth birth;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Point point;

    @Column(
            name = "gender",
            nullable = false,
            updatable = false
    )
    @Enumerated(EnumType.STRING)
    private Gender gender;

    public static User from(UserCreateCommand command) {
        requireNonNull(command, "회원 생성 명령은 null입니다.");

        User user = new User();

        user.accountId = command.accountId();
        user.email = command.email();
        user.birth = command.birth();
        user.point = Point.initialize(user);
        user.gender = command.gender();

        return user;
    }

    public void chargePoint(Money amount) {
        requireNonNull(amount, "충전할 포인트는 null일 수 없습니다.");
        this.point.increaseBalance(amount);
    }

    public void usePoint(Money amount) {
        requireNonNull(amount, "사용할 포인트는 null일 수 없습니다.");
        this.point.decreaseBalance(amount);
    }

    public Money point() {
        return this.point.getBalance();
    }
}
