package com.vins.spring_boot_training.domain.archive.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ArchiveError;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;

@Converter
public class LeaderboardConverter implements AttributeConverter<List<ScoreDto>, String> {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public String convertToDatabaseColumn(List<ScoreDto> leaderboard) {
    if (leaderboard == null) {
      return null;
    }
    try {
      return objectMapper.writeValueAsString(leaderboard);
    } catch (JsonProcessingException e) {
      throw new CustomException(ArchiveError.CONVERSION_TO_JSON_ERROR);
    }
  }

  @Override
  public List<ScoreDto> convertToEntityAttribute(String dbData) {
    if (dbData == null || dbData.isEmpty()) {
      return null;
    }
    try {
      return objectMapper.readValue(dbData, new TypeReference<>() {});
    } catch (JsonProcessingException e) {
      throw new CustomException(ArchiveError.CONVERSION_TO_ENTITY_ERROR);
    }
  }
}
