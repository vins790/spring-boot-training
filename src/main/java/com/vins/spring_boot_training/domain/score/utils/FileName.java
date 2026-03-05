package com.vins.spring_boot_training.domain.score.utils;

import com.vins.spring_boot_training.domain.score.export.ExportFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FileName {
  public String leaderboard(ExportFormat extension) {
    String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    return String.format("%s_%s.%s", "leaderboard", timestamp, extension.getExtension());
  }
}
