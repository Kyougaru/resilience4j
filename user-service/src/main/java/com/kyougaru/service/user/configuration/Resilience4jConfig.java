package com.kyougaru.service.user.configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import io.github.resilience4j.timelimiter.TimeLimiterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class Resilience4jConfig {
    @Bean
    public CircuitBreaker circuitBreaker(CircuitBreakerRegistry registry) {
        return registry.circuitBreaker("circuitBreaker", CircuitBreakerConfig.custom()
                .minimumNumberOfCalls(5).build());
    }

    @Bean
    public Retry retry(RetryRegistry registry) {
        return registry.retry("retry");
    }

    @Bean
    public TimeLimiter timeLimiter(TimeLimiterRegistry registry) {
        return registry.timeLimiter("timeLimiter");
    }

    @Bean
    public RateLimiter rateLimiter(RateLimiterRegistry registry) {
        return registry.rateLimiter("rateLimiter", RateLimiterConfig.custom()
                .limitForPeriod(2)
                .limitRefreshPeriod(Duration.ofMinutes(1))
                .build());
    }
}
