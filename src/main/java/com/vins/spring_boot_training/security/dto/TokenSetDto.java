package com.vins.spring_boot_training.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenSetDto {
  private String accessToken;
  private String refreshToken;
}
