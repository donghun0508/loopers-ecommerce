package com.loopers.fixture;

import static org.instancio.Select.field;

import com.loopers.domain.user.AccountId;
import com.loopers.domain.user.Birth;
import com.loopers.domain.user.Email;
import com.loopers.domain.user.Gender;
import com.loopers.domain.user.User;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.settings.AssignmentType;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;

public class UserFixture {

    private static final ThreadLocalRandom random = ThreadLocalRandom.current();

    private static final InstancioApi<User> CACHED_API = Instancio.of(User.class)
        .withSettings(Settings.create().set(Keys.ASSIGNMENT_TYPE, AssignmentType.METHOD))
        .supply(field(User::getAccountId), () ->
            AccountId.of(Instancio.gen().string().length(5, 10).alphaNumeric().get()))
        .supply(field(User::getEmail), () ->
            Email.of(Instancio.gen().net().email().get()))
        .supply(field(User::getBirth), () -> {
            LocalDate randomDate = Instancio.gen().temporal()
                .localDate()
                .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
                .get();
            String birthString = randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            return Birth.of(birthString);
        })
        .supply(field(User::getGender), () ->
            Instancio.gen().enumOf(Gender.class).get());

    public static Builder builder() {
        return new Builder();
    }

    public static User persistence() {
        return UserFixture.builder()
            .id(null)
            .build();
    }

    public static class Builder {

        private final InstancioApi<User> api;

        public Builder() {
            this.api = CACHED_API;
        }

        public Builder id(Long id) {
            this.api.set(field(User::getId), id);
            return this;
        }

        public User build() {
            return api.create();
        }
    }

    public static List<User> createBulk(int count) {
        return IntStream.range(1, count + 1)
            .mapToObj(i -> CACHED_API.create())
            .toList();
    }
}
