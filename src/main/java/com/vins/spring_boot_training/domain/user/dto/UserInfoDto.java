package com.vins.spring_boot_training.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoDto {
  private String username;
  private Set<String> words;
}
