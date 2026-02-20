package com.vins.spring_boot_training.domain.score.export.strategy;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.vins.spring_boot_training.domain.score.export.FileExportStrategy;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import java.util.List;

public class CsvExport<T> implements FileExportStrategy<T> {

  private final CsvMapper csvMapper;
  private final CsvSchema csvSchema;

  public CsvExport(Class<T> classType) {
    this.csvMapper = new CsvMapper();
    this.csvSchema = csvMapper.schemaFor(classType).withHeader();
  }

  @Override
  public byte[] export(List<T> items) {
    try {
      return csvMapper.writer(csvSchema).writeValueAsBytes(items);
    } catch (Exception e) {
      throw new CustomException(ExportErrors.EXPORT_ERROR);
    }
  }
}
