package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.config.Properties;
import com.vins.spring_boot_training.domain.fibonacci.FibonacciService;
import com.vins.spring_boot_training.domain.score.service.ScoreService;
import com.vins.spring_boot_training.domain.word.service.WordService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.vins.spring_boot_training.domain.wordFrequency.service.WordFrequencyService;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ScoreService Tests")
class ScoreServiceTest {

  @Mock
  private WordService wordService;

  @Mock
  private WordFrequencyService wordFrequencyService;

  @Mock
  private FibonacciService fibonacciService;

  @Mock
  private Properties properties;

  @InjectMocks
  private ScoreService scoreService;

  // For testing WordFrequencyService directly
  private WordFrequencyService realWordFrequencyService;

  @Nested
  @DisplayName("calculateFrequencyModifier Tests")
  class CalculateFrequencyModifierTests {

    @BeforeEach
    void setUp() {
      // Create real instance for testing calculateFrequencyModifier
      realWordFrequencyService = new WordFrequencyService(properties, null);
    }

    @Test
    @DisplayName("Should return 0 for null frequency")
    void shouldReturn0ForNullFrequency() {
      Long result = realWordFrequencyService.calculateFrequencyModifier(null);
      assertEquals(0L, result);
    }

    @Test
    @DisplayName("Should return 0 for frequency 0")
    void shouldReturn0ForFrequency0() {
      Long result = realWordFrequencyService.calculateFrequencyModifier(0.0);
      assertEquals(0L, result);
    }

    @Test
    @DisplayName("Should return 0 for NaN")
    void shouldReturn0ForNaN() {
      Long result = realWordFrequencyService.calculateFrequencyModifier(Double.NaN);
      assertEquals(0L, result);
    }

    @Test
    @DisplayName("Should return 0 for positive infinity")
    void shouldReturn0ForPositiveInfinity() {
      Long result = realWordFrequencyService.calculateFrequencyModifier(Double.POSITIVE_INFINITY);
      assertEquals(0L, result);
    }

    @Test
    @DisplayName("Should return 0 for negative infinity")
    void shouldReturn0ForNegativeInfinity() {
      Long result = realWordFrequencyService.calculateFrequencyModifier(Double.NEGATIVE_INFINITY);
      assertEquals(0L, result);
    }

    @Test
    @DisplayName("Should calculate modifier for frequency 0.1")
    void shouldCalculateModifierForFrequency01() {
      // frequency = 0.1000000000
      // 1st decimal: 1 * 10 = 10
      // 2nd-10th decimals: 0 * weight = 0
      // Expected: 10
      Long result = realWordFrequencyService.calculateFrequencyModifier(0.1);
      assertEquals(10L, result);
    }

    @Test
    @DisplayName("Should calculate modifier for frequency 0.123456")
    void shouldCalculateModifierForFrequency0123456() {
      // frequency = 0.1234560000
      // 1st: 1*10=10, 2nd: 2*9=18, 3rd: 3*8=24, 4th: 4*7=28, 5th: 5*6=30, 6th: 6*5=30
      // Expected: 10+18+24+28+30+30 = 140
      Long result = realWordFrequencyService.calculateFrequencyModifier(0.123456);
      assertEquals(140L, result);
    }

    @Test
    @DisplayName("Should calculate modifier for frequency 0.9999999999")
    void shouldCalculateModifierForFrequency09999999999() {
      // frequency = 0.9999999999
      // 1st: 9*10=90, 2nd: 9*9=81, 3rd: 9*8=72, 4th: 9*7=63, 5th: 9*6=54
      // 6th: 9*5=45, 7th: 9*4=36, 8th: 9*3=27, 9th: 9*2=18, 10th: 9*1=9
      // Expected: 90+81+72+63+54+45+36+27+18+9 = 495
      Long result = realWordFrequencyService.calculateFrequencyModifier(0.9999999999);
      assertEquals(495L, result);
    }

    @Test
    @DisplayName("Should calculate modifier for very small frequency")
    void shouldCalculateModifierForVerySmallFrequency() {
      // frequency = 0.0000000001
      // Only 10th decimal is non-zero: 1 * 1 = 1
      Long result = realWordFrequencyService.calculateFrequencyModifier(0.0000000001);
      assertEquals(1L, result);
    }

    @Test
    @DisplayName("Should calculate modifier for frequency 0.0123456789")
    void shouldCalculateModifierForFrequency00123456789() {
      // frequency = 0.0123456789
      // 1st: 0*10=0, 2nd: 1*9=9, 3rd: 2*8=16, 4th: 3*7=21, 5th: 4*6=24
      // 6th: 5*5=25, 7th: 6*4=24, 8th: 7*3=21, 9th: 8*2=16, 10th: 9*1=9
      // Expected: 0+9+16+21+24+25+24+21+16+9 = 165
      Long result = realWordFrequencyService.calculateFrequencyModifier(0.0123456789);
      assertEquals(165L, result);
    }
  }

  @Nested
  @DisplayName("calculateWordScore Tests")
  class CalculateWordScoreTests {

    @BeforeEach
    void setUp() {
      when(properties.getWordLanguage()).thenReturn("pl");
    }

    @Test
    @DisplayName("Should calculate word score with valid frequency")
    void shouldCalculateWordScoreWithValidFrequency() {
      String word = "test";
      when(wordFrequencyService.getWordFrequencyModifier("test", "pl"))
          .thenReturn(140L);
      when(fibonacciService.getFibonacci(4)).thenReturn(3L);

      Long result = scoreService.calculateWordScore(word);

      // frequencyModifier for 0.123456 = 140
      // fibonacci for length 4 = 3
      // score = 140 * 3 = 420
      assertEquals(420L, result);
    }

    @Test
    @DisplayName("Should return 0 for word with zero frequency")
    void shouldReturn0ForWordWithZeroFrequency() {
      String word = "rare";
      when(wordFrequencyService.getWordFrequencyModifier("rare", "pl"))
          .thenReturn(0L);
      when(fibonacciService.getFibonacci(4)).thenReturn(3L);

      Long result = scoreService.calculateWordScore(word);

      assertEquals(0L, result);
    }

    @Test
    @DisplayName("Should calculate word score with high frequency")
    void shouldCalculateWordScoreWithHighFrequency() {
      String word = "a";
      when(wordFrequencyService.getWordFrequencyModifier("a", "pl"))
          .thenReturn(90L);
      when(fibonacciService.getFibonacci(1)).thenReturn(1L);

      Long result = scoreService.calculateWordScore(word);

      // frequencyModifier for 0.9 = 9*10 = 90
      // fibonacci for length 1 = 1
      // score = 90 * 1 = 90
      assertEquals(90L, result);
    }

    @Test
    @DisplayName("Should handle long word with low frequency")
    void shouldHandleLongWordWithLowFrequency() {
      String word = "extraordinary";
      when(wordFrequencyService.getWordFrequencyModifier("extraordinary", "pl"))
          .thenReturn(7L);
      when(fibonacciService.getFibonacci(13)).thenReturn(233L);

      Long result = scoreService.calculateWordScore(word);

      // frequencyModifier for 0.0001 = 1*7 = 7
      // fibonacci for length 13 = 233
      // score = 7 * 233 = 1631
      assertEquals(1631L, result);
    }
  }

  @Nested
  @DisplayName("calculateUserScore Tests")
  class CalculateUserScoreTests {

    @BeforeEach
    void setUp() {
      lenient().when(properties.getWordLanguage()).thenReturn("pl");
    }

    @Test
    @DisplayName("Should return 0 for user with no words")
    void shouldReturn0ForUserWithNoWords() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of());

      long result = scoreService.calculateUserScore(userId);

      assertEquals(0L, result);
    }

    @Test
    @DisplayName("Should calculate score for user with one word")
    void shouldCalculateScoreForUserWithOneWord() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of("test"));
      when(wordFrequencyService.getWordFrequencyModifier("test", "pl"))
          .thenReturn(10L);
      when(fibonacciService.getFibonacci(4)).thenReturn(3L);

      long result = scoreService.calculateUserScore(userId);

      // frequencyModifier for 0.1 = 10
      // fibonacci for length 4 = 3
      // wordScore = 10 * 3 = 30
      // No bonus (word length <= 5 and <= 10)
      assertEquals(30L, result);
    }

    @Test
    @DisplayName("Should apply bonus for word with length > 5")
    void shouldApplyBonusForWordLengthGreaterThan5() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of("longer"));
      when(wordFrequencyService.getWordFrequencyModifier("longer", "pl"))
          .thenReturn(10L);
      when(fibonacciService.getFibonacci(6)).thenReturn(8L);

      long result = scoreService.calculateUserScore(userId);

      // frequencyModifier for 0.1 = 10
      // fibonacci for length 6 = 8
      // wordScore = 10 * 8 = 80
      // bonus: wordScore * 2 = 160 (for length > 5)
      assertEquals(160L, result);
    }

    @Test
    @DisplayName("Should apply bonus for word with length > 10")
    void shouldApplyBonusForWordLengthGreaterThan10() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of("extraordinary"));
      when(wordFrequencyService.getWordFrequencyModifier("extraordinary", "pl"))
          .thenReturn(10L);
      when(fibonacciService.getFibonacci(13)).thenReturn(233L);

      long result = scoreService.calculateUserScore(userId);

      // frequencyModifier for 0.1 = 10
      // fibonacci for length 13 = 233
      // wordScore = 10 * 233 = 2330
      // bonus: wordScore * 3 = 6990 (for length > 10)
      assertEquals(6990L, result);
    }

    @Test
    @DisplayName("Should apply bonus only once for length > 10")
    void shouldApplyBonusOnlyOnceForLengthGreaterThan10() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(
          Set.of("extraordinary", "unbelievable")
      );
      when(wordFrequencyService.getWordFrequencyModifier("extraordinary", "pl"))
          .thenReturn(10L);
      when(wordFrequencyService.getWordFrequencyModifier("unbelievable", "pl"))
          .thenReturn(10L);
      when(fibonacciService.getFibonacci(13)).thenReturn(233L);
      when(fibonacciService.getFibonacci(12)).thenReturn(144L);

      long result = scoreService.calculateUserScore(userId);

      // Scenario 1: "extraordinary" processed first
      // - extraordinary (13): 10 * 233 = 2330, * 3 = 6990
      // - unbelievable (12): 10 * 144 = 1440, * 2 = 2880
      // Total: 6990 + 2880 = 9870

      // Scenario 2: "unbelievable" processed first
      // - unbelievable (12): 10 * 144 = 1440, * 3 = 4320
      // - extraordinary (13): 10 * 233 = 2330, * 2 = 4660
      // Total: 4320 + 4660 = 8980

      // Note: Due to Set ordering, result depends on which word is processed first
      assertTrue(result == 9870L || result == 8980L);
    }

    @Test
    @DisplayName("Should apply bonus only once for length > 5")
    void shouldApplyBonusOnlyOnceForLengthGreaterThan5() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(
          Set.of("longer", "second")
      );
      when(wordFrequencyService.getWordFrequencyModifier("longer", "pl"))
          .thenReturn(10L);
      when(wordFrequencyService.getWordFrequencyModifier("second", "pl"))
          .thenReturn(10L);
      when(fibonacciService.getFibonacci(6)).thenReturn(8L);

      long result = scoreService.calculateUserScore(userId);

      // First word: 10 * 8 = 80, * 2 = 160
      // Second word: 10 * 8 = 80 (no bonus multiplier)
      // Total: 160 + 80 = 240
      assertEquals(240L, result);
    }

    @Test
    @DisplayName("Should skip words with zero score")
    void shouldSkipWordsWithZeroScore() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(
          Set.of("valid", "zero")
      );
      when(wordFrequencyService.getWordFrequencyModifier("valid", "pl"))
          .thenReturn(10L);
      when(wordFrequencyService.getWordFrequencyModifier("zero", "pl"))
          .thenReturn(0L);
      when(fibonacciService.getFibonacci(5)).thenReturn(5L);
      when(fibonacciService.getFibonacci(4)).thenReturn(3L);

      long result = scoreService.calculateUserScore(userId);

      // First word: 10 * 5 = 50 (no bonus for length <= 5)
      // Second word: 0 (skipped)
      // Total: 50
      assertEquals(50L, result);
    }

    @Test
    @DisplayName("Should calculate complex score with multiple words and bonuses")
    void shouldCalculateComplexScoreWithMultipleWordsAndBonuses() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(
          new HashSet<>(Arrays.asList("extraordinary", "longer", "test", "a"))
      );
      when(wordFrequencyService.getWordFrequencyModifier("extraordinary", "pl"))
          .thenReturn(52L);
      when(wordFrequencyService.getWordFrequencyModifier("longer", "pl"))
          .thenReturn(20L);
      when(wordFrequencyService.getWordFrequencyModifier("test", "pl"))
          .thenReturn(45L);
      when(wordFrequencyService.getWordFrequencyModifier("a", "pl"))
          .thenReturn(90L);

      when(fibonacciService.getFibonacci(13)).thenReturn(233L);
      when(fibonacciService.getFibonacci(6)).thenReturn(8L);
      when(fibonacciService.getFibonacci(4)).thenReturn(3L);
      when(fibonacciService.getFibonacci(1)).thenReturn(1L);

      long result = scoreService.calculateUserScore(userId);

      // extraordinary (length 13): frequencyModifier=52 (1*10+2*9+3*8), fibonacci=233, score=52*233=12116, *3 = 36348
      // longer (length 6): frequencyModifier=20 (2*10), fibonacci=8, score=20*8=160, *2 = 320
      // test (length 4): frequencyModifier=45 (0*10+5*9), fibonacci=3, score=45*3=135
      // a (length 1): frequencyModifier=90 (9*10), fibonacci=1, score=90*1=90
      // Total: 36348 + 320 + 135 + 90 = 36893
      assertEquals(36893L, result);
    }

    @Test
    @DisplayName("Should calculate score for word with length < 5 (no bonus)")
    void shouldCalculateScoreForWordWithLengthLessThan5() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of("cat"));
      when(wordFrequencyService.getWordFrequencyModifier("cat", "pl"))
          .thenReturn(50L);
      when(fibonacciService.getFibonacci(3)).thenReturn(2L);

      long result = scoreService.calculateUserScore(userId);

      // frequencyModifier for 0.5 = 5*10 = 50
      // fibonacci for length 3 = 2
      // wordScore = 50 * 2 = 100
      // No bonus (length < 5)
      assertEquals(100L, result);
    }

    @Test
    @DisplayName("Should calculate score for word with length exactly 5 (no bonus)")
    void shouldCalculateScoreForWordWithLengthExactly5() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of("house"));
      when(wordFrequencyService.getWordFrequencyModifier("house", "pl"))
          .thenReturn(30L);
      when(fibonacciService.getFibonacci(5)).thenReturn(5L);

      long result = scoreService.calculateUserScore(userId);

      // frequencyModifier for 0.3 = 3*10 = 30
      // fibonacci for length 5 = 5
      // wordScore = 30 * 5 = 150
      // No bonus (length = 5, not > 5)
      assertEquals(150L, result);
    }

    @Test
    @DisplayName("Should calculate score for word with length between 5 and 10 (bonus x2)")
    void shouldCalculateScoreForWordWithLengthBetween5And10() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of("computer"));
      when(wordFrequencyService.getWordFrequencyModifier("computer", "pl"))
          .thenReturn(40L);
      when(fibonacciService.getFibonacci(8)).thenReturn(21L);

      long result = scoreService.calculateUserScore(userId);

      // frequencyModifier for 0.4 = 4*10 = 40
      // fibonacci for length 8 = 21
      // wordScore = 40 * 21 = 840
      // Bonus: 840 * 2 = 1680 (length 8, which is > 5 and < 10)
      assertEquals(1680L, result);
    }

    @Test
    @DisplayName("Should calculate score for word with length exactly 10 (bonus x2)")
    void shouldCalculateScoreForWordWithLengthExactly10() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of("strawberry"));
      when(wordFrequencyService.getWordFrequencyModifier("strawberry", "pl"))
          .thenReturn(20L);
      when(fibonacciService.getFibonacci(10)).thenReturn(55L);

      long result = scoreService.calculateUserScore(userId);

      // frequencyModifier for 0.2 = 2*10 = 20
      // fibonacci for length 10 = 55
      // wordScore = 20 * 55 = 1100
      // Bonus: 1100 * 2 = 2200 (length 10, which is > 5 but not > 10)
      assertEquals(2200L, result);
    }

    @Test
    @DisplayName("Should calculate score for word with length > 10 (bonus x3)")
    void shouldCalculateScoreForWordWithLengthGreaterThan10() {
      long userId = 1L;
      when(wordService.getWords(userId)).thenReturn(Set.of("encyclopedia"));
      when(wordFrequencyService.getWordFrequencyModifier("encyclopedia", "pl"))
          .thenReturn(55L);
      when(fibonacciService.getFibonacci(12)).thenReturn(144L);

      long result = scoreService.calculateUserScore(userId);

      // frequencyModifier for 0.15 = 1*10 + 5*9 = 10 + 45 = 55
      // fibonacci for length 12 = 144
      // wordScore = 55 * 144 = 7920
      // Bonus: 7920 * 3 = 23760 (length > 10)
      assertEquals(23760L, result);
    }

    @Test
    @DisplayName("Should handle multiple words with different lengths correctly")
    void shouldHandleMultipleWordsWithDifferentLengthsCorrectly() {
      long userId = 1L;
      // cat (3), house (5), computer (8), encyclopedia (12)
      when(wordService.getWords(userId)).thenReturn(
          new HashSet<>(Arrays.asList("cat", "house", "computer", "encyclopedia"))
      );
      when(wordFrequencyService.getWordFrequencyModifier("cat", "pl"))
          .thenReturn(10L);
      when(wordFrequencyService.getWordFrequencyModifier("house", "pl"))
          .thenReturn(10L);
      when(wordFrequencyService.getWordFrequencyModifier("computer", "pl"))
          .thenReturn(10L);
      when(wordFrequencyService.getWordFrequencyModifier("encyclopedia", "pl"))
          .thenReturn(10L);

      when(fibonacciService.getFibonacci(3)).thenReturn(2L);
      when(fibonacciService.getFibonacci(5)).thenReturn(5L);
      when(fibonacciService.getFibonacci(8)).thenReturn(21L);
      when(fibonacciService.getFibonacci(12)).thenReturn(144L);

      long result = scoreService.calculateUserScore(userId);

      // encyclopedia (12): 10 * 144 = 1440, * 3 = 4320
      // computer (8): 10 * 21 = 210, * 2 = 420
      // house (5): 10 * 5 = 50 (no bonus)
      // cat (3): 10 * 2 = 20 (no bonus)
      // Total: 4320 + 420 + 50 + 20 = 4810
      assertEquals(4810L, result);
    }
  }
}
