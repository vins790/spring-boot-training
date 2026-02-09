package com.vins.spring_boot_training.exception;

public class FibonacciNotNullException extends RuntimeException  {
  public FibonacciNotNullException(String message) {
    super(message);
  }

  public FibonacciNotNullException(String message, Throwable cause) {
    super(message, cause);
  }

  public FibonacciNotNullException(Throwable cause) {
    super(cause);
  }
}
