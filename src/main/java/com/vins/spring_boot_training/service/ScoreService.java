package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.dto.ScoreDto;
import com.vins.spring_boot_training.entity.Score;
import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.repository.ScoreRepository;
import com.vins.spring_boot_training.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScoreService {
  private final ScoreRepository scoreRepository;
  private final WordsService wordsService;
  private final UserService userService;
  private final UsersRepository usersRepository;

  public long calculateUserScore(long userId) {
    AtomicInteger bonus10 = new AtomicInteger(1);
    AtomicInteger bonus5 = new AtomicInteger(1);
    return wordsService
        .getWords(userId)
        .stream()
        .map(String::length)
        .reduce(0, (score, length) -> {
          if (length > 10 & bonus10.get() > 0) {
            bonus10.getAndDecrement();
            return score + 3;
          };
          if (length > 5 & bonus5.get() > 0) {
            bonus5.getAndDecrement();
            return score + 2;
          }
          return score + 1;
        });
  }

  @Scheduled(cron = "0 * * * * *")
  public void scheduledScoreCalculation() {
    deleteOrphanScores();
    Set<User> users = userService.getAllUsers();
    users.forEach(user -> {
      scoreRepository.findById(user.getId()).ifPresentOrElse(
          this::updateChangedScore,
          () -> addScore(user)
      );
    });
  }

  public Long getUserScore(Long userId) {
    return scoreRepository.findById(userId)
        .map(Score::getScore)
        .orElse(0L);
  }

  public List<ScoreDto> getLeaderboard(Long size) {
    return scoreRepository.findAll().stream()
        .sorted((s1, s2) -> Long.compare(s2.getScore(), s1.getScore()))
        .limit(size)
        .map(score -> new ScoreDto(score.getUserId(), score.getScore()))
        .collect(Collectors.toList());
  }

  private void deleteOrphanScores() {
    Set<Long> usersIds = userService.getAllUsersIds();
    scoreRepository.findAll().forEach(score -> {
      if (!usersIds.contains(score.getUserId())) {
        scoreRepository.delete(score);
      }
    });
  }

  private void updateChangedScore (Score score) {
    long newScore = calculateUserScore(score.getUserId());
    if (!score.getScore().equals(newScore)) {
      score.setScore(newScore);
      scoreRepository.save(score);
    }
  }

  private void addScore(User user) {
    long newScore = calculateUserScore(user.getId());
    scoreRepository.save(new Score(user, newScore));
  }
}
