package com.loopers.interfaces.api.user.fixture;

import static org.instancio.Select.field;

import com.loopers.interfaces.api.user.UserV1Dto;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class UserV1DtoFixture {

    public static class SignUpRequest {
        public static InstancioApi<UserV1Dto.SignUpRequest> complete() {
            return Instancio.of(UserV1Dto.SignUpRequest.class)
                .generate(field(UserV1Dto.SignUpRequest::userId), gen -> gen.string()
                    .length(5, 10)
                    .alphaNumeric()
                )
                .generate(field(UserV1Dto.SignUpRequest::email), gen -> gen.net().email())
                .generate(field(UserV1Dto.SignUpRequest::birth), gen -> gen.temporal()
                    .localDate()
                    .range(LocalDate.of(1950, 1, 1), LocalDate.of(2005, 12, 31))
                    .as(date -> date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                );
        }
    }
}
