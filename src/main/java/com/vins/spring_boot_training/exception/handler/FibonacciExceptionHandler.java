package com.vins.spring_boot_training.exception.handler;

import com.vins.spring_boot_training.exception.FibonacciNotNullException;
import com.vins.spring_boot_training.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface FibonacciExceptionHandler {
  @ExceptionHandler(FibonacciNotNullException.class)
  default ResponseEntity<ErrorResponse> handleException(FibonacciNotNullException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }
}
