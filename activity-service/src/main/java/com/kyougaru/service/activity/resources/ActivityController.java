package com.kyougaru.service.activity.resources;

import com.kyougaru.service.activity.model.Activity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class ActivityController {
    @GetMapping("/activityRetry")
    public ResponseEntity<Activity> activityRetry() {
        Random random = new Random();
        if (random.nextInt(0, 2) == 0) {
            throw new RuntimeException();
        }
        else {
            var activity = new Activity("test", "test", "test", "test", 1, 1.0, 1.0);
            return ResponseEntity.ok().body(activity);
        }
    }

    @GetMapping("/activityTimeout")
    public ResponseEntity<Activity> activityTimeout() throws InterruptedException {
        Thread.sleep(80000);
        return ResponseEntity.ok().body(new Activity("test", "test", "test", "test", 1, 1.0, 1.0));
    }

    @GetMapping("/activityCircuitBreaker")
    public ResponseEntity<Activity> circuitBreaker() {
        throw new RuntimeException();
    }

    @GetMapping("/rateLimiter")
    public ResponseEntity<Activity> rateLimiter() {
        return ResponseEntity.ok().body(new Activity("test", "test", "test", "test", 1, 1.0, 1.0));
    }
}
