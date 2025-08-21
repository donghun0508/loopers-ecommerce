package com.loopers.resilience;

import static io.github.resilience4j.retry.RetryConfig.*;

import com.loopers.resilience.config.Resilience4jConfig.CustomCircuitBreakerConfig;
import com.loopers.resilience.config.Resilience4jConfig.CustomRetryConfig;
import com.loopers.resilience.config.Resilience4jConfig.CustomTimeLimiterConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Resilience4jConfigFactory {

    private final CircuitBreakerRegistry circuitBreakerRegistry;
    private final RetryRegistry retryRegistry;
    private final TimeLimiterRegistry timeLimiterRegistry;
    private final ExceptionClassConverter exceptionClassConverter;

    public CircuitBreaker createCircuitBreaker(String name, CircuitBreakerConfig customConfig) {
        if (customConfig == null) {
            return circuitBreakerRegistry.circuitBreaker(name);
        }
        return circuitBreakerRegistry.circuitBreaker(name, customConfig);
    }

    public CircuitBreaker createCircuitBreaker(String name, Consumer<CircuitBreakerConfig.Builder> customizer) {
        var defaultConfig = circuitBreakerRegistry.getDefaultConfig();
        var builder = CircuitBreakerConfig.from(defaultConfig);
        customizer.accept(builder);
        return circuitBreakerRegistry.circuitBreaker(name, builder.build());
    }

    public Retry createRetry(String name, RetryConfig customConfig) {
        if (customConfig == null) {
            return retryRegistry.retry(name);
        }
        return retryRegistry.retry(name, customConfig);
    }

    public Retry createRetry(String name, Consumer<RetryConfig.Builder> customizer) {
        var defaultConfig = retryRegistry.getDefaultConfig();
        var builder = RetryConfig.from(defaultConfig);
        customizer.accept(builder);
        return retryRegistry.retry(name, builder.build());
    }

    public TimeLimiter createTimeLimiter(String name, TimeLimiterConfig customConfig) {
        if (customConfig == null) {
            return timeLimiterRegistry.timeLimiter(name);
        }
        return timeLimiterRegistry.timeLimiter(name, customConfig);
    }

    public TimeLimiter createTimeLimiter(String name, Consumer<TimeLimiterConfig.Builder> customizer) {
        var defaultConfig = timeLimiterRegistry.getDefaultConfig();
        var builder = TimeLimiterConfig.from(defaultConfig);
        customizer.accept(builder);
        return timeLimiterRegistry.timeLimiter(name, builder.build());
    }

    public CircuitBreaker createCircuitBreaker(String name, CustomCircuitBreakerConfig customConfig) {
        return createCircuitBreaker(name, builder -> applyCustomCircuitBreakerConfig(builder, customConfig));
    }

    public Retry createRetry(String name, CustomRetryConfig customConfig) {
        return createRetry(name, builder -> applyCustomRetryConfig(builder, customConfig, null));
    }

    public Retry createRetry(String name, CustomRetryConfig customConfig, Predicate<Throwable> manualPredicate) {
        return createRetry(name, builder -> applyCustomRetryConfig(builder, customConfig, manualPredicate));
    }

    public TimeLimiter createTimeLimiter(String name, CustomTimeLimiterConfig customConfig) {
        return createTimeLimiter(name, builder -> applyCustomTimeLimiterConfig(builder, customConfig));
    }

    private void applyCustomCircuitBreakerConfig(CircuitBreakerConfig.Builder builder, CustomCircuitBreakerConfig customConfig) {
        Optional.ofNullable(customConfig.getFailureRateThreshold()).ifPresent(builder::failureRateThreshold);
        Optional.ofNullable(customConfig.getSlowCallRateThreshold()).ifPresent(builder::slowCallRateThreshold);
        Optional.ofNullable(customConfig.getSlowCallDurationThreshold()).map(Duration::ofMillis).ifPresent(builder::slowCallDurationThreshold);
        Optional.ofNullable(customConfig.getWaitDurationInOpenState()).map(Duration::ofMillis).ifPresent(builder::waitDurationInOpenState);
        Optional.ofNullable(customConfig.getMinimumNumberOfCalls()).ifPresent(builder::minimumNumberOfCalls);
        Optional.ofNullable(customConfig.getSlidingWindowSize()).ifPresent(builder::slidingWindowSize);
        Optional.ofNullable(customConfig.getPermittedNumberOfCallsInHalfOpenState()).ifPresent(builder::permittedNumberOfCallsInHalfOpenState);
        Optional.ofNullable(customConfig.getAutomaticTransitionFromOpenToHalfOpenEnabled()).ifPresent(builder::automaticTransitionFromOpenToHalfOpenEnabled);

        if (customConfig.getRecordExceptions() != null && !customConfig.getRecordExceptions().isEmpty()) {
            builder.recordExceptions(exceptionClassConverter.convertToClasses(customConfig.getRecordExceptions()));
        }
    }

    private void applyCustomRetryConfig(RetryConfig.Builder builder, CustomRetryConfig customConfig, Predicate<Throwable> manualPredicate) {
        Optional.ofNullable(customConfig.getMaxAttempts()).ifPresent(builder::maxAttempts);

        if (customConfig.getWaitDuration() != null && customConfig.getExponentialBackoffMultiplier() != null) {
            builder.intervalFunction(IntervalFunction.ofExponentialBackoff(
                Duration.ofMillis(customConfig.getWaitDuration()),
                customConfig.getExponentialBackoffMultiplier()));
        } else if (customConfig.getWaitDuration() != null) {
            builder.waitDuration(Duration.ofMillis(customConfig.getWaitDuration()));
        }

        if (Boolean.TRUE.equals(customConfig.getManual()) && manualPredicate != null) {
            builder.retryOnException(manualPredicate);
        } else if (!Boolean.TRUE.equals(customConfig.getManual())) {
            if (customConfig.getRetryExceptions() != null && !customConfig.getRetryExceptions().isEmpty()) {
                builder.retryExceptions(exceptionClassConverter.convertToClasses(customConfig.getRetryExceptions()));
            }
            if (customConfig.getIgnoreExceptions() != null && !customConfig.getIgnoreExceptions().isEmpty()) {
                builder.ignoreExceptions(exceptionClassConverter.convertToClasses(customConfig.getIgnoreExceptions()));
            }
        }
    }

    private void applyCustomTimeLimiterConfig(TimeLimiterConfig.Builder builder, CustomTimeLimiterConfig customConfig) {
        Optional.ofNullable(customConfig.getTimeoutDuration()).map(Duration::ofMillis).ifPresent(builder::timeoutDuration);
        Optional.ofNullable(customConfig.getCancelRunningFuture()).ifPresent(builder::cancelRunningFuture);
    }
}
