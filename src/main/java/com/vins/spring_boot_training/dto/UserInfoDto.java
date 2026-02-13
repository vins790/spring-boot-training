package com.vins.spring_boot_training.dto;

import com.vins.spring_boot_training.entity.Word;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoDto {
  private String username;
  private Set<Word> words;
}
