package com.kyougaru.service.user.resources;

import com.kyougaru.service.user.model.Activity;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.timelimiter.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.*;
import java.util.function.Supplier;

@RestController
public class UserController {
    @Autowired
    private CircuitBreaker circuitBreaker;

    @Autowired
    private Retry retry;

    @Autowired
    private TimeLimiter timeLimiter;

    @Autowired
    private RateLimiter rateLimiter;

    @Autowired
    private RestTemplate restTemplate;

    private final String API = "http://localhost:8090/";

    //Tenta um número de vezes até sucesso ou falha
    @GetMapping("/retry")
    public ResponseEntity<String> retry() {
        Supplier<ResponseEntity<Activity>> supplier = () -> restTemplate.getForEntity(API + "activityRetry", Activity.class);

        var retrySupplier = Retry.decorateSupplier(retry, supplier);

        return ResponseEntity.ok().body(retrySupplier.get().getBody().activity());
    }

    //Lança uma exceção TimeoutException se exceder o limite de tempo
    @GetMapping("/timeout")
    public ResponseEntity<String> timeout() throws ExecutionException, InterruptedException, TimeoutException {
        Supplier<ResponseEntity<Activity>> supplier = () -> restTemplate.getForEntity(API + "activityTimeout", Activity.class);
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        CompletableFuture<ResponseEntity<Activity>> result = timeLimiter.executeCompletionStage(scheduler, () -> CompletableFuture.supplyAsync(supplier)).toCompletableFuture();

        ResponseEntity<Activity> response = result.get(5, TimeUnit.SECONDS);
        return ResponseEntity.ok().body(response.getBody().activity());
    }

    //Lança uma exceção CallNotPermittedException após ficar OPEN
    @GetMapping("/circuitBreaker")
    public ResponseEntity<String> circuitBreaker() {
        Supplier<ResponseEntity<Activity>> supplier = () -> restTemplate.getForEntity(API + "activityCircuitBreaker", Activity.class);

        var circuitBreakerSupplier = CircuitBreaker.decorateSupplier(circuitBreaker, supplier);

        return ResponseEntity.ok().body(circuitBreakerSupplier.get().getBody().activity());
    }

    //Lança uma exceção quando não permite mais chamadas
    @GetMapping("/rateLimiter")
    public ResponseEntity<String> rateLimiter() {
        Supplier<ResponseEntity<Activity>> supplier = () -> restTemplate.getForEntity(API + "rateLimiter", Activity.class);

        var rateLimiterSupplier = RateLimiter.decorateSupplier(rateLimiter, supplier);

        return ResponseEntity.ok().body(rateLimiterSupplier.get().getBody().activity());
    }
}
