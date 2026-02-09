package com.vins.spring_boot_training.exception.response;

public class FibonacciErrorResponse {
  private String message;
  private long timeStamp;

  public FibonacciErrorResponse(String message, long timeStamp) {
    this.message = message;
    this.timeStamp = timeStamp;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  public void setTimeStamp(long timeStamp) {
    this.timeStamp = timeStamp;
  }
}
