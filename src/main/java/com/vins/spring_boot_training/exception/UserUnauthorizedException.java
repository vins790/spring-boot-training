package com.vins.spring_boot_training.exception;

public class UserUnauthorizedException extends RuntimeException  {
  public UserUnauthorizedException(String message) {
    super(message);
  }

  public UserUnauthorizedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserUnauthorizedException(Throwable cause) {
    super(cause);
  }
}
