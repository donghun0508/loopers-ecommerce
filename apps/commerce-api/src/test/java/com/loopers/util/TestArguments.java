package com.loopers.util;

import org.junit.jupiter.params.provider.Arguments;

import java.util.stream.Stream;

public class TestArguments {

    public static Stream<Arguments> args(String... values) {
        return Stream.of(values).map(Arguments::of);
    }
}
