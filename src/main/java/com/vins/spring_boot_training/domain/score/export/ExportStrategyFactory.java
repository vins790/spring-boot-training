package com.vins.spring_boot_training.domain.score.export;

import com.vins.spring_boot_training.domain.score.export.strategy.CsvExport;
import com.vins.spring_boot_training.domain.score.export.strategy.JsonExport;
import com.vins.spring_boot_training.domain.score.export.strategy.XmlExport;

import java.util.Map;

public class ExportStrategyFactory {

  public static <T> Map<ExportFormat, FileExportStrategy<T>> create(Class<T> classType) {
    return Map.of(
        ExportFormat.CSV, new CsvExport<T>(classType),
        ExportFormat.JSON, new JsonExport<T>(),
        ExportFormat.XML, new XmlExport<T>()
    );
  }
}
