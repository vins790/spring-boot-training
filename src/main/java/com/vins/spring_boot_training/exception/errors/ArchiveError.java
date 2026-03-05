package com.vins.spring_boot_training.exception.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ArchiveError implements CustomError {

  CONVERSION_TO_JSON_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to convert data to JSON format."),
  CONVERSION_TO_ENTITY_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to convert JSON data to entity format.");

  @Getter
  private final HttpStatus httpStatus;
  @Getter
  private final String errorMessage;

  ArchiveError(HttpStatus httpStatus, String errorMessage) {
    this.httpStatus = httpStatus;
    this.errorMessage = errorMessage;
  }

}
