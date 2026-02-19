package com.vins.spring_boot_training.cron.service;

import com.vins.spring_boot_training.domain.score.service.ScoreService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CronService {
  private ScoreService scoreService;

  @Scheduled(cron = "0 * * * * *")
  public void scheduledScoreCalculation() {
    scoreService.scoreCalculation();
  }
}
