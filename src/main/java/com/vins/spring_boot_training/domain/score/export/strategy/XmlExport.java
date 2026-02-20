package com.vins.spring_boot_training.domain.score.export.strategy;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.vins.spring_boot_training.domain.score.dto.XmlExportDto;
import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.domain.score.export.FileExportStrategy;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class XmlExport implements FileExportStrategy {

  private final XmlMapper xmlMapper;

  public XmlExport() {
    this.xmlMapper = new XmlMapper();
    this.xmlMapper.findAndRegisterModules();
  }

  @Override
  public byte[] export(List<ScoreDto> leaderboard) {
    try {
      XmlExportDto xmlExportDto = new XmlExportDto(leaderboard);
      return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(xmlExportDto);
    } catch (Exception e) {
      throw new CustomException(ExportErrors.EXPORT_ERROR);
    }
  }
}
