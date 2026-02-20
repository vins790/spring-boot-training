package com.vins.spring_boot_training.domain.score.export.strategy;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vins.spring_boot_training.domain.score.dto.XmlExportDto;
import com.vins.spring_boot_training.domain.score.export.FileExportStrategy;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import java.util.List;

public class XmlExport<T> implements FileExportStrategy<T> {

  private final XmlMapper xmlMapper;

  public XmlExport() {
    this.xmlMapper = new XmlMapper();
    this.xmlMapper.findAndRegisterModules();
  }

  @Override
  public byte[] export(List<T> items) {
    try {
      XmlExportDto<T> xmlExportDto = new XmlExportDto<>(items);
      return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(xmlExportDto);
    } catch (Exception e) {
      throw new CustomException(ExportErrors.EXPORT_ERROR);
    }
  }
}
