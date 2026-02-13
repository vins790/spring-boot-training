package com.vins.spring_boot_training.exception;

public class UserInvalidPasswordException extends RuntimeException  {
  public UserInvalidPasswordException(String message) {
    super(message);
  }

  public UserInvalidPasswordException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserInvalidPasswordException(Throwable cause) {
    super(cause);
  }
}
