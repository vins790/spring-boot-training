package com.vins.spring_boot_training.exception.handler;

import com.vins.spring_boot_training.exception.FibonacciNotNullException;
import com.vins.spring_boot_training.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FibonacciExceptionHandler {
  @ExceptionHandler(FibonacciNotNullException.class)
  public ResponseEntity<ErrorResponse> handleException(FibonacciNotNullException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    ErrorResponse errorResponse = new ErrorResponse(
        "Error occurred while processing the request",
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
