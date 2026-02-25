package com.vins.spring_boot_training.domain.score.service;

import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.domain.score.utils.WorkbookFactory;
import org.apache.poi.ss.usermodel.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;;

@Service
public class WorkbookService {

  private final ScoreService scoreService;

  public WorkbookService(@Lazy ScoreService scoreService) {
    this.scoreService = scoreService;
  }

  public byte[] getUserScoresWithPercentageWorkbook() {
    List<ScoreDto> data = scoreService.getLeaderboard();
    Map<String, BiFunction<ScoreDto, Integer, Object>> columnDefinitions = new LinkedHashMap<>();

    columnDefinitions.put("User ID", (scoreDto, _index) -> scoreDto.getUserId().toString());
    columnDefinitions.put("Score", (scoreDto, _index) -> scoreDto.getScore().doubleValue());
    columnDefinitions.put("% of Total", (_scoreDto, index) ->
        String.format("=(B%d/SUM($B$2:$B$%d))*100", index + 2, data.size() + 1));

    return WorkbookFactory.create("Users Score Percentage", data, columnDefinitions);
  }

  public byte[] getUserScoresWithLeaderboardPosition() {
    List<ScoreDto> data = scoreService.getLeaderboard();
    Map<String, BiFunction<ScoreDto, Integer, Object>> columnDefinitions = new LinkedHashMap<>();

    columnDefinitions.put("User ID", (scoreDto, _index) -> scoreDto.getUserId().toString());
    columnDefinitions.put("Score", (scoreDto, _index) -> scoreDto.getScore().doubleValue());
    columnDefinitions.put("Position", (_scoreDto, index) -> String.format("=RANK(B%d,$B$2:$B$%d,0)", index + 2, data.size() + 1));

    return WorkbookFactory.create("Users Ranking Position", data, columnDefinitions);
  }
}