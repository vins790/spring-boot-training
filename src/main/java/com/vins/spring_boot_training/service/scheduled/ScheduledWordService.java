package com.vins.spring_boot_training.service.scheduled;

import com.vins.spring_boot_training.service.ScoreService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ScheduledWordService {
  private ScoreService scoreService;

  @Scheduled(cron = "0 * * * * *")
  public void scheduledScoreCalculation() {
    scoreService.scoreCalculation();
  }
}
