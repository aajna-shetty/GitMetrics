package com.example.GitMetrics.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<?> handleNotFound(){
        return ResponseEntity.status(404).body(Map.of("error", "Repository not found on GitHub."));
    }
    @ExceptionHandler(HttpClientErrorException.Forbidden.class)
    public ResponseEntity<?> handleRateLimit(){
            return ResponseEntity.status(403).body(Map.of("error","GitHub API rate limit exceeded."));
    }
}
