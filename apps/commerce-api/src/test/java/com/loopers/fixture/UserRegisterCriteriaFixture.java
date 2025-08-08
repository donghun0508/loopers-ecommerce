package com.loopers.fixture;

import com.loopers.application.user.CriteriaCommand.UserRegisterCriteria;
import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.Birth;
import com.loopers.domain.user.Email;
import com.loopers.domain.user.Gender;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.settings.AssignmentType;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.instancio.Select.field;

public class UserRegisterCriteriaFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final InstancioApi<UserRegisterCriteria> api;

        public Builder() {
            this.api = Instancio.of(UserRegisterCriteria.class)
                    .withSettings(Settings.create().set(Keys.ASSIGNMENT_TYPE, AssignmentType.METHOD))
                    .supply(field(UserRegisterCriteria::accountId), () ->
                            AccountId.of(Instancio.gen().string().length(5, 10).alphaNumeric().get()))
                    .supply(field(UserRegisterCriteria::email), () ->
                            Email.of(Instancio.gen().net().email().get()))
                    .supply(field(UserRegisterCriteria::birth), () -> {
                        LocalDate randomDate = Instancio.gen().temporal()
                                .localDate()
                                .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
                                .get();
                        String birthString = randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        return Birth.of(birthString);
                    })
                    .supply(field(UserRegisterCriteria::gender), () ->
                            Instancio.gen().enumOf(Gender.class).get());
        }

        public Builder accountId(AccountId accountId) {
            this.api.set(field(UserRegisterCriteria::accountId), accountId);
            return this;
        }

        public Builder email(Email email) {
            this.api.set(field(UserRegisterCriteria::email), email);
            return this;
        }

        public Builder birth(Birth birth) {
            this.api.set(field(UserRegisterCriteria::birth), birth);
            return this;
        }

        public Builder gender(Gender gender) {
            this.api.set(field(UserRegisterCriteria::gender), gender);
            return this;
        }

        public UserRegisterCriteria build() {
            return api.create();
        }
    }
}
