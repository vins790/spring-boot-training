package com.vins.spring_boot_training.domain.archive.service;

import com.vins.spring_boot_training.domain.archive.entity.ArchivalLeaderboard;
import com.vins.spring_boot_training.domain.archive.repository.LaderboardArchiveRepository;
import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.domain.score.export.ExportFormat;
import com.vins.spring_boot_training.domain.score.service.ExportService;
import com.vins.spring_boot_training.domain.score.service.ScoreService;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.ExportErrors;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ArchiveService {
  private final ExportService exportService;
  private final ScoreService scoreService;
  private final LaderboardArchiveRepository leaderboardArchiveRepository;

  private static final String ARCHIVE_BASE_NAME = "leaderboard";
  private static final String ARCHIVE_BASE_PATH = "archives";
  private static final DateTimeFormatter FOLDER_NAME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss").withZone(ZoneId.systemDefault());

  public ArchiveService(ExportService exportService,
                        @Lazy ScoreService scoreService,
                        LaderboardArchiveRepository leaderboardArchiveRepository) {
    this.exportService = exportService;
    this.scoreService = scoreService;
    this.leaderboardArchiveRepository = leaderboardArchiveRepository;
  }

  @Transactional(rollbackFor = Exception.class)
  public void archive() {
    List<ScoreDto> leaderboard = scoreService.getLeaderboard();

    leaderboardArchiveRepository.save(new ArchivalLeaderboard(leaderboard));
    saveLeaderboardToFiles(leaderboard);
    scoreService.dropLeaderboard();
  }

  private void saveLeaderboardToFiles(List<ScoreDto> leaderboard) {
    try {
      Instant now = Instant.now();
      String folderName = FOLDER_NAME_FORMATTER.format(now);
      Path archiveFolder = Paths.get(ARCHIVE_BASE_PATH, folderName);
      Files.createDirectories(archiveFolder);

      for (ExportFormat format : ExportFormat.values()) {
        String fileName = ARCHIVE_BASE_NAME + "." + format.getExtension();
        byte[] data = exportService.exportLeaderboard(format, (long) leaderboard.size());
        Path filePath = archiveFolder.resolve(fileName);
        Files.write(filePath, data);
      }
    } catch (IOException e) {
      throw new CustomException(ExportErrors.EXPORT_ERROR);
    }
  }

  public Instant getLastArchiveTimestamp() {
    return leaderboardArchiveRepository.findTopByOrderByTimestampDesc()
        .map(ArchivalLeaderboard::getTimestamp)
        .orElse(null);
  }
}
