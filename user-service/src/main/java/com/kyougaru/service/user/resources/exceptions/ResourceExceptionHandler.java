package com.kyougaru.service.user.resources.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.concurrent.TimeoutException;

@ControllerAdvice
public class ResourceExceptionHandler {
    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<StandardError> timeOut(TimeoutException e) {
        var error = "Timeout";
        var status = HttpStatus.REQUEST_TIMEOUT;
        var err = new StandardError(Instant.now(), status.value(), error, e.getMessage());
        return ResponseEntity.status(status).body(err);
    }
}
