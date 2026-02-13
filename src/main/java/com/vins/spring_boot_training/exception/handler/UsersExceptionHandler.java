package com.vins.spring_boot_training.exception.handler;

import com.vins.spring_boot_training.exception.UserAlreadyExistsException;
import com.vins.spring_boot_training.exception.UserInvalidPasswordException;
import com.vins.spring_boot_training.exception.UserInvalidUsernameException;
import com.vins.spring_boot_training.exception.UserUnauthorizedException;
import com.vins.spring_boot_training.exception.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public interface UsersExceptionHandler {
  @ExceptionHandler(UserAlreadyExistsException.class)
  default ResponseEntity<ErrorResponse> handleException(UserAlreadyExistsException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserInvalidUsernameException.class)
  default ResponseEntity<ErrorResponse> handleException(UserInvalidUsernameException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserInvalidPasswordException.class)
  default ResponseEntity<ErrorResponse> handleException(UserInvalidPasswordException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(UserUnauthorizedException.class)
  default ResponseEntity<ErrorResponse> handleException(UserUnauthorizedException e) {
    ErrorResponse errorResponse = new ErrorResponse(
        e.getMessage(),
        System.currentTimeMillis()
    );
    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }
}
