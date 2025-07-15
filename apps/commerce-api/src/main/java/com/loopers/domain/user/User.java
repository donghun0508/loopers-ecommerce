package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.domain.user.UserCommand.Create;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

@Entity
@Table(name = "members")
public class User extends BaseEntity {

    private String userId;
    private String email;
    private String birth;

    protected User() {

    }

    private User(String userId, String email, String birth) {
        this.userId = userId;
        this.email = email;
        this.birth = birth;
    }

    public static User of(Create command) {
        validate(command);
        return new User(command.userId(), command.email(), command.birth());
    }

    private static void validate(Create command) {
        Validator.userId(command.userId());
        Validator.email(command.email());
        Validator.birth(command.birth());
    }

    private static class Validator {

        private static final Pattern USER_ID_REGEX = Pattern.compile("^[a-zA-Z0-9]{1,10}$");
        private static final Pattern EMAIL_REGEX = Pattern.compile(
            "^[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9]|[a-zA-Z0-9_-]*\\.[a-zA-Z0-9](?:[a-zA-Z0-9_-]*[a-zA-Z0-9])*)*@[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9]|[a-zA-Z0-9-]*\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])*)*\\.[a-zA-Z]{2,6}$"
        );
        private static final Pattern BIRTH_REGEX = Pattern.compile("^\\d{4}-\\d{2}-\\d{2}$");

        private Validator() {
        }

        public static void userId(String userId) {
            if (isNullOrBlank(userId) ||
                !USER_ID_REGEX.matcher(userId).matches()) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }

        public static void email(String email) {
            if (isNullOrBlank(email) ||
                !EMAIL_REGEX.matcher(email).matches()
            ) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }

        public static void birth(String birth) {
            validateBirthFormat(birth);
            validateBirthDate(birth);
        }

        private static boolean isNullOrBlank(String value) {
            return value == null || value.isBlank();
        }

        private static void validateBirthFormat(String birth) {
            if (isNullOrBlank(birth) ||
                !BIRTH_REGEX.matcher(birth).matches()
            ) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }

        private static void validateBirthDate(String birth) {
            try {
                LocalDate.parse(birth);
            } catch (DateTimeParseException e) {
                throw new CoreException(ErrorType.INVALID_INPUT);
            }
        }
    }
}
