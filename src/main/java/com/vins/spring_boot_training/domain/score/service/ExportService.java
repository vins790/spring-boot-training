package com.vins.spring_boot_training.domain.score.service;

import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.domain.score.export.ExportFormat;
import com.vins.spring_boot_training.domain.score.export.ExportStrategyFactory;
import com.vins.spring_boot_training.domain.score.export.FileExportStrategy;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ExportService {

  private final ScoreService scoreService;

  public ExportService(ScoreService scoreService) {
    this.scoreService = scoreService;
  }

  public byte[] exportLeaderboard(ExportFormat format, Long size) {
    Map<ExportFormat, FileExportStrategy<ScoreDto>> strategies = ExportStrategyFactory.create(ScoreDto.class);
    FileExportStrategy<ScoreDto> strategy = strategies.get(format);

    if (strategy == null) {
      throw new CustomException(ExportErrors.UNSUPPORTED_FORMAT);
    }

    List<ScoreDto> leaderboard = scoreService.getLeaderboard(size);
    return strategy.export(leaderboard);
  }
}
