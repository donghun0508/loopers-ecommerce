package com.loopers.domain.point.fixture;

import com.loopers.domain.point.PointCommand;
import org.instancio.Instancio;
import org.instancio.InstancioApi;

public class PointCommandFixture {

    public static class Create {

        public static InstancioApi<PointCommand.Create> complete() {
            return Instancio.of(PointCommand.Create.class);
        }
    }
}
