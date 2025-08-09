package com.loopers.config.annotations;

import com.loopers.testcontainers.MySqlTestContainersConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag("integration")
@Import(MySqlTestContainersConfig.class)
@SpringBootTest
@ActiveProfiles("test")
public @interface IntegrationTest {

}
