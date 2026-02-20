package com.vins.spring_boot_training.domain.score.config;

import com.vins.spring_boot_training.domain.score.export.ExportFormat;
import com.vins.spring_boot_training.domain.score.export.FileExportStrategy;
import com.vins.spring_boot_training.domain.score.export.strategy.CsvExport;
import com.vins.spring_boot_training.domain.score.export.strategy.JsonExpot;
import com.vins.spring_boot_training.domain.score.export.strategy.XmlExport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class ExportConfig {

  @Bean
  public Map<ExportFormat, FileExportStrategy> exportStrategies(
      CsvExport csvExport,
      JsonExpot jsonExpot,
      XmlExport xmlExport) {
    return Map.of(
        ExportFormat.CSV, csvExport,
        ExportFormat.JSON, jsonExpot,
        ExportFormat.XML, xmlExport
    );
  }
}
