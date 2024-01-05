package com.kyougaru.service.user.resources.exceptions;

import java.time.Instant;

public record StandardError(Instant timestamp, Integer status, String error, String message) {
}
