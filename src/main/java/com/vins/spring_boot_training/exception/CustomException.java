package com.vins.spring_boot_training.exception;

import com.vins.spring_boot_training.exception.errors.CustomError;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CustomException extends RuntimeException {
  private final CustomError customError;

  public CustomException(CustomError customError) {
    super(customError.getErrorMessage());
    this.customError = customError;
  }
}