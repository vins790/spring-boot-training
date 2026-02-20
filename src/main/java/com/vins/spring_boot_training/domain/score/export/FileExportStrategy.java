package com.vins.spring_boot_training.domain.score.export;

import com.vins.spring_boot_training.domain.score.dto.ScoreDto;

import java.util.List;

public interface FileExportStrategy {
  byte[] export(List<ScoreDto> leaderboard);
}
