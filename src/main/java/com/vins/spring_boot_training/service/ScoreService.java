package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.config.Properties;
import com.vins.spring_boot_training.dto.ScoreDto;
import com.vins.spring_boot_training.entity.Score;
import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.repository.ScoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@AllArgsConstructor
public class ScoreService {
  private final ScoreRepository scoreRepository;
  private final WordsService wordsService;
  private final UserService userService;
  private final WordFrequencyService wordFrequencyService;
  private final FibonacciService fibonacciService;
  private final Properties properties;

  private Long calculateWordScore(String word) {
    Long fibonacci = fibonacciService.getFibonacci(word.length());
    Long frequencyModifier = calculateFrequencyModifier(wordFrequencyService
        .getWordFrequency(word, properties.getWordLanguage())
        .getFrequency());

    return frequencyModifier * fibonacci;
  }

  private Long calculateFrequencyModifier(Double frequency) {
    if (frequency == null || frequency == 0.0 || !Double.isFinite(frequency)) return 0L;

    String formatted = String.format(Locale.US, "%.10f", frequency);
    String[] parts = formatted.split("\\.");

    if (parts.length < 2) return 0L;

    final String decimalPart = parts[1].length() < 10
        ? String.format("%-10s", parts[1]).replace(' ', '0')
        : parts[1];

    return IntStream.range(0, 10)
        .mapToLong(i -> (long) Character.getNumericValue(decimalPart.charAt(i)) * (10 - i))
        .sum();
  }

  public long calculateUserScore(long userId) {
    AtomicInteger bonus10 = new AtomicInteger(1);
    AtomicInteger bonus5 = new AtomicInteger(1);
    return wordsService
        .getWords(userId)
        .stream()
        .mapToLong(word -> {
          long wordBaseScore = calculateWordScore(word);
          if (wordBaseScore == 0) return 0L;
          if (word.length() > 10 && bonus10.get() > 0) {
            bonus10.getAndDecrement();
            return wordBaseScore * 3L;
          }
          if (word.length() > 5 && bonus5.get() > 0) {
            bonus5.getAndDecrement();
            return wordBaseScore * 2L;
          }
          return wordBaseScore;
        }).sum();
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

  private void updateChangedScore(Score score) {
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
