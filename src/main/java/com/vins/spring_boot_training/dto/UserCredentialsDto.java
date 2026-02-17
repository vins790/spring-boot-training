package com.vins.spring_boot_training.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCredentialsDto {
  @NotBlank(message = "Username is required")
  @Size(min = 3, max = 50)
  private String username;

  @NotBlank(message = "Password is required")
  @Size(min = 6)
  private String password;
}
