package com.vins.spring_boot_training.service;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
@AllArgsConstructor
public class ScoreService {

  private final WordsService wordsService;
  private final UserService userService;

  private long calculateUserScore(long userId) {
    AtomicInteger bonus10 = new AtomicInteger(1);
    AtomicInteger bonus5 = new AtomicInteger(1);
    return wordsService
        .getWords(userId)
        .stream()
        .map(String::length)
        .reduce(0, (length, score) -> {
          if (length > 10 & bonus10.getAndDecrement() > 0) return score + 3;
          if (length > 5 & bonus5.getAndDecrement() > 0) return score + 2;
          return score + 1;
        });
  }

  @Scheduled(cron = "0 * * * * *")
  public void scheduledScoreCalculation() {
    userService.getAllUsersIds().forEach(userId -> {
      long score = calculateUserScore(userId);
      System.out.println("Score for user " + userId + " : " + score);
    });
  }
}
