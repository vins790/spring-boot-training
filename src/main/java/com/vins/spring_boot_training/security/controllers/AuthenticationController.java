package com.vins.spring_boot_training.security.controllers;

import com.vins.spring_boot_training.security.dto.UserCredentialsDto;
import com.vins.spring_boot_training.security.dto.TokenDto;
import com.vins.spring_boot_training.security.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthenticationController {

  private final AuthenticationService authenticationService;

  @Operation(summary = "Register a new user", description = "Create a new user account with the provided credentials")
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  public void addUser(@RequestBody UserCredentialsDto credentials) {
    authenticationService.registerUser(credentials);
  }

  @Operation(summary = "Login a user", description = "Authenticate a user and return a JWT token")
  @PostMapping("/login")
  @ResponseStatus(HttpStatus.OK)
  public TokenDto login(@Valid @RequestBody UserCredentialsDto credentials) {
    return authenticationService.login(credentials);
  }
}
