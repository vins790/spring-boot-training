package com.vins.spring_boot_training.domain.score.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ScoreDto {
  private Long userId;
  private Long score;
}
