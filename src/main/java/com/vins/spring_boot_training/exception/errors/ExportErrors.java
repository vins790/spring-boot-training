package com.vins.spring_boot_training.exception.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ExportErrors implements CustomError {

  EXPORT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "User already exists!"),
  UNSUPPORTED_FORMAT(HttpStatus.INTERNAL_SERVER_ERROR, "Format is not supported!");

  @Getter
  private final HttpStatus httpStatus;
  @Getter
  private final String errorMessage;

  ExportErrors(HttpStatus httpStatus, String errorMessage) {
    this.httpStatus = httpStatus;
    this.errorMessage = errorMessage;
  }

}
