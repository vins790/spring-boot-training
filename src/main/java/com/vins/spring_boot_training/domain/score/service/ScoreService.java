package com.vins.spring_boot_training.domain.score.service;

import com.vins.spring_boot_training.config.Properties;
import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import com.vins.spring_boot_training.domain.score.entity.Score;
import com.vins.spring_boot_training.domain.user.entity.User;
import com.vins.spring_boot_training.domain.fibonacci.FibonacciService;
import com.vins.spring_boot_training.domain.score.repository.ScoreRepository;
import com.vins.spring_boot_training.domain.word.service.WordService;
import com.vins.spring_boot_training.domain.score.utils.BonusCalculator;
import com.vins.spring_boot_training.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import com.vins.spring_boot_training.domain.word.service.WordFrequencyService;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScoreService {
  private final ScoreRepository scoreRepository;
  private final WordService wordService;
  private final UserService userService;
  private final WordFrequencyService wordFrequencyService;
  private final FibonacciService fibonacciService;
  private final Properties properties;

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

  private void saveScore(User user) {
    scoreRepository.findById(user.getId()).ifPresentOrElse(
        this::updateChangedScore,
        () -> addScore(user)
    );
  }

  private void addScore(User user) {
    long newScore = calculateUserScore(user.getId());
    scoreRepository.save(new Score(user, newScore));
  }

  public Long calculateWordScore(String word) {
    Long fibonacci = fibonacciService.getFibonacci(word.length());
    Long frequencyModifier = wordFrequencyService.getWordFrequencyModifier(word, properties.getWordLanguage());
    return frequencyModifier * fibonacci;
  }

  public long calculateUserScore(long userId) {
    BonusCalculator bonusCalculator = new BonusCalculator();
    Set<String> words = wordService.getWords(userId);
    Map<String, Long> wordScores = words.stream()
        .collect(Collectors.toMap(
            word -> word,
            this::calculateWordScore
        ));

    return wordScores
        .entrySet()
        .stream()
        .mapToLong(bonusCalculator::applyBonus)
        .sum();
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

  public void scoreCalculation() {
    deleteOrphanScores();
    Set<User> users = userService.getAllUsers();
    users.forEach(this::saveScore);
  }
}
