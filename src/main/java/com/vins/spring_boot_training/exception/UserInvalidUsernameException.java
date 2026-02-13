package com.vins.spring_boot_training.exception;

public class UserInvalidUsernameException extends RuntimeException  {
  public UserInvalidUsernameException(String message) {
    super(message);
  }

  public UserInvalidUsernameException(String message, Throwable cause) {
    super(message, cause);
  }

  public UserInvalidUsernameException(Throwable cause) {
    super(cause);
  }
}
