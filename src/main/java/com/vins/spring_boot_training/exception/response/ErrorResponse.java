package com.vins.spring_boot_training.exception.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse {
  private String message;
  private long timeStamp;
}
