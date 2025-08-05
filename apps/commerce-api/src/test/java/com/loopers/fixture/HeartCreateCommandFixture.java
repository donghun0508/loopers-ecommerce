package com.loopers.fixture;

import com.loopers.domain.heart.HeartCreateCommand;
import com.loopers.domain.heart.Target;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

import static org.instancio.Select.field;

public class HeartCreateCommandFixture {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final InstancioApi<HeartCreateCommand> api;

        public Builder() {
            this.api = Instancio.of(HeartCreateCommand.class);
        }

        public Builder userId(Long userId) {
            this.api.set(field(HeartCreateCommand::userId), userId);
            return this;
        }

        public Builder target(Target target) {
            this.api.set(field(HeartCreateCommand::target), target);
            return this;
        }

        public HeartCreateCommand build() {
            return api.create();
        }
    }
}
