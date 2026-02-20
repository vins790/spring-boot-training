package com.vins.spring_boot_training.domain.score.export.strategy;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vins.spring_boot_training.domain.score.export.FileExportStrategy;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import java.util.List;

public class JsonExport<T> implements FileExportStrategy<T> {

  private final ObjectMapper objectMapper;

  public JsonExport() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.findAndRegisterModules();
  }

  @Override
  public byte[] export(List<T> items) {
    try {
      return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(items);
    } catch (Exception e) {
      throw new CustomException(ExportErrors.EXPORT_ERROR);
    }
  }
}
