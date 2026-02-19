package com.vins.spring_boot_training.exception.handler;

import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.response.ErrorResponse;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Log
public class CustomExceptionHandler {

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(CustomException e) {
    log.info("[LOG INFO] " + e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(
        e.getCustomError().getErrorMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, e.getCustomError().getHttpStatus());
  }

  @ExceptionHandler
  public ResponseEntity<ErrorResponse> handleException(Exception e) {
    log.info("[LOG INFO] " + e.getMessage());
    ErrorResponse errorResponse = new ErrorResponse(
        "Error occurred while processing the request",
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}