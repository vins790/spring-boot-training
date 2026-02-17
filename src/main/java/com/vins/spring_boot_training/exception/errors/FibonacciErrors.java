package com.vins.spring_boot_training.exception.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum FibonacciErrors implements CustomError {

  NEGATIVE_NUMBER(HttpStatus.BAD_REQUEST, "Negative number is not allowed in Fibonacci sequence");

  @Getter
  private final HttpStatus httpStatus;
  @Getter
  private final String errorMessage;


  FibonacciErrors(HttpStatus httpStatus, String errorMessage) {
    this.errorMessage = errorMessage;
    this.httpStatus = httpStatus;
  }
}