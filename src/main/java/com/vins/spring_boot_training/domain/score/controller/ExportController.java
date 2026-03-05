package com.vins.spring_boot_training.domain.score.controller;

import com.vins.spring_boot_training.domain.score.service.ScoreWorkbookService;
import com.vins.spring_boot_training.domain.score.utils.ExportResponse;
import com.vins.spring_boot_training.domain.score.export.ExportFormat;
import com.vins.spring_boot_training.domain.score.service.ExportService;
import com.vins.spring_boot_training.domain.score.utils.FileName;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/export")
public class ExportController {

  private final ExportService exportService;
  private final FileName fileNameGenerator;
  private final ScoreWorkbookService scoreWorkbookService;

  @GetMapping("/leaderboard/json/{size}")
  public ResponseEntity<byte[]> exportLeaderboardJson(@PathVariable Long size) {
    byte[] data = exportService.exportLeaderboard(ExportFormat.JSON, size);
    String fileName = fileNameGenerator.leaderboard(ExportFormat.JSON);
    return ExportResponse.build(data, fileName, MediaType.APPLICATION_JSON);
  }

  @GetMapping("/leaderboard/xml/{size}")
  public ResponseEntity<byte[]> exportLeaderboardXml(@PathVariable Long size) {
    byte[] data = exportService.exportLeaderboard(ExportFormat.XML, size);
    String fileName = fileNameGenerator.leaderboard(ExportFormat.XML);
    return ExportResponse.build(data, fileName, MediaType.APPLICATION_XML);
  }

  @GetMapping("/leaderboard/csv/{size}")
  public ResponseEntity<byte[]> exportLeaderboardCsv(@PathVariable Long size) {
    byte[] data = exportService.exportLeaderboard(ExportFormat.CSV, size);
    String fileName = fileNameGenerator.leaderboard(ExportFormat.CSV);
    return ExportResponse.build(data, fileName, MediaType.parseMediaType("text/csv"));
  }

  @GetMapping("/leaderboard/xlsx/percentage")
  public ResponseEntity<byte[]> exportLeaderboardWithPercentage() {
    byte[] data = scoreWorkbookService.getUserScoresWithPercentage();
    String fileName = fileNameGenerator.leaderboard(ExportFormat.XLSX);
    return ExportResponse.build(data, fileName, MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
  }

  @GetMapping("/leaderboard/xlsx/position")
  public ResponseEntity<byte[]> exportLeaderboardWithPositon() {
    byte[] data = scoreWorkbookService.getUserScoresWithRank();
    String fileName = fileNameGenerator.leaderboard(ExportFormat.XLSX);
    return ExportResponse.build(data, fileName, MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
  }

  @GetMapping("/leaderboard/xlsx/complete")
  public ResponseEntity<byte[]> exportCompleteLeaderboard() {
    byte[] data = scoreWorkbookService.getCompleteUserScores();
    String fileName = fileNameGenerator.leaderboard(ExportFormat.XLSX);
    return ExportResponse.build(data, fileName, MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
  }
}
