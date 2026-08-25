package com.digitalbank.account.exception.handler;

import com.digitalbank.account.exception.*;
import com.digitalbank.account.exception.dto.ErrorRs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}