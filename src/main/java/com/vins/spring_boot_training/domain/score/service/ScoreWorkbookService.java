package com.vins.spring_boot_training.domain.score.service;

import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.domain.score.utils.WorkbookFactory;
import com.vins.spring_boot_training.domain.score.enums.ScoreWorkbookColumns;
import com.vins.spring_boot_training.domain.user.service.UserService;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@Service
public class ScoreWorkbookService {

  private final ScoreService scoreService;
  private final UserService userService;

  public ScoreWorkbookService(@Lazy ScoreService scoreService, UserService userService) {
    this.scoreService = scoreService;
    this.userService = userService;
  }

  private Map<ScoreWorkbookColumns, BiFunction<ScoreDto, Integer, Object>> columnsDefinitionsFactory(List<ScoreDto> data) {
    Map<ScoreWorkbookColumns, BiFunction<ScoreDto, Integer, Object>> columnDefinitions = new LinkedHashMap<>();

    columnDefinitions.put(ScoreWorkbookColumns.ID,
        (scoreDto, _index) -> scoreDto.getUserId().toString());

    columnDefinitions.put(ScoreWorkbookColumns.USERNAME,
        (scoreDto, _index) -> userService.getUserByUserId(scoreDto.getUserId()).getUsername());

    columnDefinitions.put(ScoreWorkbookColumns.SCORE,
        (scoreDto, _index) -> scoreDto.getScore().doubleValue());

    columnDefinitions.put(ScoreWorkbookColumns.PERCENTAGE,
        (_scoreDto, index) -> String.format("=ROUND((C%d/SUM($C$2:$C$%d))*100,2)", index + 2, data.size() + 1));

    columnDefinitions.put(ScoreWorkbookColumns.RANK,
        (_scoreDto, index) -> String.format("=RANK(C%d,$C$2:$C$%d,0)", index + 2, data.size() + 1));

    return columnDefinitions;
  }

  private byte[] scoreWorkbookFactory(String workbookName, List<ScoreWorkbookColumns> columns) {
    List<ScoreDto> data = scoreService.getLeaderboard();
    Map<ScoreWorkbookColumns, BiFunction<ScoreDto, Integer, Object>> columnDefinitions =
        columnsDefinitionsFactory(data);
    Map<String, BiFunction<ScoreDto, Integer, Object>> selectedColumnDefinitions = new LinkedHashMap<>();

    columns.forEach(column -> {
      if (!columnDefinitions.containsKey(column)) {
        throw new CustomException(ExportErrors.WORKBOOK_UNSUPPORTED_COLUMN);
      }
      selectedColumnDefinitions.put(column.getColumnName(), columnDefinitions.get(column));
    });

    return WorkbookFactory.create(workbookName, data, selectedColumnDefinitions);
  }

  public byte[] getUserScoresWithPercentage() {
    List<ScoreWorkbookColumns> columns = new ArrayList<>();
    columns.add(ScoreWorkbookColumns.ID);
    columns.add(ScoreWorkbookColumns.USERNAME);
    columns.add(ScoreWorkbookColumns.SCORE);
    columns.add(ScoreWorkbookColumns.PERCENTAGE);
    return scoreWorkbookFactory("Leaderboard with Percentage", columns);
  }

  public byte[] getUserScoresWithRank() {
    List<ScoreWorkbookColumns> columns = new ArrayList<>();
    columns.add(ScoreWorkbookColumns.ID);
    columns.add(ScoreWorkbookColumns.USERNAME);
    columns.add(ScoreWorkbookColumns.SCORE);
    columns.add(ScoreWorkbookColumns.RANK);
    return scoreWorkbookFactory("Leaderboard with Rank", columns);
  }

  public byte[] getCompleteUserScores() {
    List<ScoreWorkbookColumns> columns = new ArrayList<>();
    columns.add(ScoreWorkbookColumns.ID);
    columns.add(ScoreWorkbookColumns.USERNAME);
    columns.add(ScoreWorkbookColumns.SCORE);
    columns.add(ScoreWorkbookColumns.PERCENTAGE);
    columns.add(ScoreWorkbookColumns.RANK);
    return scoreWorkbookFactory("Complete Leaderboard", columns);
  }



}