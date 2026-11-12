package com.digitalbank.account.exception.handler;

import com.digitalbank.account.exception.*;
import com.digitalbank.account.exception.dto.ErrorRs;
import feign.FeignException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ErrorRs> handleAccountNotFoundException (AccountNotFoundException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Exception trigger date: {}. Exception content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(AccountFrozenException.class)
    public ResponseEntity<ErrorRs> handleAccountFrozenException (AccountFrozenException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Exception trigger date: {}. Exception content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ErrorRs> handleInsufficientFundsException (InsufficientFundsException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Exception trigger date: {}. Exception content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(UnsupportedCurrencyException.class)
    public ResponseEntity<ErrorRs> handleUnsupportedCurrencyException (UnsupportedCurrencyException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Exception trigger date: {}. Exception content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
    @ExceptionHandler(PaymentSystemException.class)
    public ResponseEntity<ErrorRs> handlePaymentSystemException(PaymentSystemException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Exception trigger date: {}. Exception content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value()));
    }
    @ExceptionHandler(ConstraintViolationException.class) //без нее будет выкидывать 500
    public ResponseEntity<ErrorRs> handleConstraintViolationException(ConstraintViolationException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Validation error at {}. Content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorRs> handleMethodArgumentNotValidException(MethodArgumentNotValidException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        String message = error.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .reduce((a, b) -> a + "; " + b)
                .orElse("Validation failed");
        log.error("Validation error at {}. Content: {}", timestamp, message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRs(timestamp, message, HttpStatus.BAD_REQUEST.value()));
    }
    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<ErrorRs> handleMissingRequestHeaderException(MissingRequestHeaderException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Missing header at {}. Content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.BAD_REQUEST.value()));
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorRs> handleUserNotFoundException(UserNotFoundException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("User not found at {}. Content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(UserBlockedException.class)
    public ResponseEntity<ErrorRs> handleUserBlockedException(UserBlockedException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("User blocked at {}. Content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.FORBIDDEN.value()));
    }

    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<ErrorRs> handleFeignNotFound(FeignException.NotFound error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Feign 404 at {}. Content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorRs(timestamp, "User not found in user-service", HttpStatus.NOT_FOUND.value()));
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorRs> handleFeignException(FeignException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("Feign call failed at {}. Status: {}, content: {}",
                timestamp, error.status(), error.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorRs(timestamp, "User service unavailable", HttpStatus.SERVICE_UNAVAILABLE.value()));
    }
    @ExceptionHandler(UserServiceUnavailableException.class)
    public ResponseEntity<ErrorRs> handleUserServiceUnavailableException(UserServiceUnavailableException error) {
        OffsetDateTime timestamp = OffsetDateTime.now();
        log.error("User service unavailable at {}. Content: {}", timestamp, error.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new ErrorRs(timestamp, error.getMessage(), HttpStatus.SERVICE_UNAVAILABLE.value()));
    }
}