package com.vins.spring_boot_training.domain.score.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScoreWorkbookColumns {
  ID("User ID"),
  USERNAME("Username"),
  SCORE("Score"),
  PERCENTAGE("% of Total"),
  RANK("Rank");

  private final String columnName;
}
