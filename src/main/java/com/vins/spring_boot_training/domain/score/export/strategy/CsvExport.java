package com.vins.spring_boot_training.domain.score.export.strategy;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.domain.score.export.FileExportStrategy;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CsvExport implements FileExportStrategy {

  private final CsvMapper csvMapper;
  private final CsvSchema schema;

  public CsvExport() {
    this.csvMapper = new CsvMapper();
    this.schema = csvMapper.schemaFor(ScoreDto.class).withHeader();
  }

  @Override
  public byte[] export(List<ScoreDto> leaderboard) {
    try {
      return csvMapper.writer(schema).writeValueAsBytes(leaderboard);
    } catch (Exception e) {
      throw new CustomException(ExportErrors.EXPORT_ERROR);
    }
  }
}
