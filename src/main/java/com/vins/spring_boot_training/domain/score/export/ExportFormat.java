package com.vins.spring_boot_training.domain.score.export;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ExportFormat {
  CSV("csv"),
  JSON("json"),
  XML("xml"),
  XLSX("xlsx");

  private final String extension;
}
