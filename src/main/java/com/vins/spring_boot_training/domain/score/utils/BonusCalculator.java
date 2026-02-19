package com.vins.spring_boot_training.domain.score.utils;

import java.util.Map;

public class BonusCalculator {
  private int bonus10;
  private int bonus5;

  public BonusCalculator() {
    this.bonus10 = 1;
    this.bonus5 = 1;
  }

  public long applyBonus(String word, long score) {
    if (score == 0) return 0L;
    if (word.length() > 10 && bonus10 > 0) {
      bonus10--;
      return score * 3L;
    }
    if (word.length() > 5 && bonus5 > 0) {
      bonus5--;
      return score * 2L;
    }
    return score;
  }

  public long applyBonus(Map.Entry<String, Long> wordScore) {
    String word = wordScore.getKey();
    long score = wordScore.getValue();
    return applyBonus(word, score);
  }
}
