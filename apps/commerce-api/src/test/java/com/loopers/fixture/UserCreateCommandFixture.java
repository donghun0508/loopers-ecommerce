//package com.loopers.fixture;
//
//import com.loopers.domain.user.*;
//import org.instancio.Instancio;
//import org.instancio.InstancioApi;
//import org.instancio.settings.AssignmentType;
//import org.instancio.settings.Keys;
//import org.instancio.settings.Settings;
//
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//
//import static org.instancio.Select.field;
//
//public class UserCreateCommandFixture {
//
//    public static Builder builder() {
//        return new Builder();
//    }
//
//    public static class Builder {
//        private final InstancioApi<UserCreateCommand> api;
//
//        public Builder() {
//            this.api = Instancio.of(UserCreateCommand.class)
//                    .withSettings(Settings.create().set(Keys.ASSIGNMENT_TYPE, AssignmentType.METHOD))
//                    .supply(field(UserCreateCommand::accountId), () ->
//                            AccountId.of(Instancio.gen().string().length(5, 10).alphaNumeric().get()))
//                    .supply(field(UserCreateCommand::email), () ->
//                            Email.of(Instancio.gen().net().email().get()))
//                    .supply(field(UserCreateCommand::birth), () -> {
//                        LocalDate randomDate = Instancio.gen().temporal()
//                                .localDate()
//                                .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
//                                .get();
//                        String birthString = randomDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//                        return Birth.of(birthString);
//                    })
//                    .supply(field(UserCreateCommand::gender), () ->
//                            Instancio.gen().enumOf(Gender.class).get());
//        }
//
//        public Builder accountId(AccountId accountId) {
//            this.api.set(field(UserCreateCommand::accountId), accountId);
//            return this;
//        }
//
//        public Builder email(Email email) {
//            this.api.set(field(UserCreateCommand::email), email);
//            return this;
//        }
//
//        public Builder birth(Birth birth) {
//            this.api.set(field(UserCreateCommand::birth), birth);
//            return this;
//        }
//
//        public Builder gender(Gender gender) {
//            this.api.set(field(UserCreateCommand::gender), gender);
//            return this;
//        }
//
//        public UserCreateCommand build() {
//            return api.create();
//        }
//    }
//}
