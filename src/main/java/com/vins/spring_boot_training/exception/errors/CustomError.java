package com.vins.spring_boot_training.exception.errors;

import org.springframework.http.HttpStatus;

public interface CustomError {
  String getErrorMessage();
  HttpStatus getHttpStatus();
}
