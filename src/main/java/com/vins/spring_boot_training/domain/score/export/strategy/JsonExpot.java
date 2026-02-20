package com.vins.spring_boot_training.domain.score.export.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.domain.score.export.FileExportStrategy;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JsonExpot implements FileExportStrategy {

  private final ObjectMapper objectMapper;

  public JsonExpot() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.findAndRegisterModules();
  }

  @Override
  public byte[] export(List<ScoreDto> leaderboard) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(leaderboard);
    } catch (Exception e) {
      throw new CustomException(ExportErrors.EXPORT_ERROR);
    }
  }
}
