package com.vins.spring_boot_training.domain.user.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserRole {
  USER("ROLE_USER"),
  ADMIN("ROLE_ADMIN");

  private final String authority;
}
