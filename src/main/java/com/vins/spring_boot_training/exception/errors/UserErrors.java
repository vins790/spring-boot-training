package com.vins.spring_boot_training.exception.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum UserErrors implements CustomError {

  USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "User already exists!"),
  USER_INVALID_USERNAME(HttpStatus.BAD_REQUEST, "Invalid username"),
  USER_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "Invalid password"),
  USER_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized user"),
  USER_ROLE_ALREADY_ADDED(HttpStatus.BAD_REQUEST, "User already has this role");


  @Getter
  private final HttpStatus httpStatus;
  @Getter
  private final String errorMessage;

  UserErrors(HttpStatus httpStatus, String errorMessage) {
    this.httpStatus = httpStatus;
    this.errorMessage = errorMessage;
  }

}
