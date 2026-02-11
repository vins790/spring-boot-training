package com.vins.spring_boot_training.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWordsServiceTest {

  private InMemoryWordsService wordsService;

  @BeforeEach
  void setUp() {
    wordsService = new InMemoryWordsService();
  }

  @ParameterizedTest
  @DisplayName("Should handle strings with no words")
  @NullAndEmptySource
  @ValueSource(strings = {"!@#$2%^&32*()_+", "123 456 789"})
  void shouldHandleEmptyOrNullStrings(String sentence) {
    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertTrue(words.isEmpty());
  }

  @Test
  @DisplayName("Should return empty set when no words saved")
  void shouldReturnEmptySetWhenNoWordsSaved() {
    // when
    Set<String> words = wordsService.getWords();

    // then
    assertNotNull(words);
    assertTrue(words.isEmpty());
  }

  @ParameterizedTest
  @DisplayName("Should extract and save words correctly")
  @MethodSource("provideSentencesWithExpectedWords")
  void shouldExtractAndSaveWordsCorrectly(String sentence, Set<String> expectedWords) {
    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(expectedWords.size(), words.size());
    assertTrue(words.containsAll(expectedWords));
  }

  private static Stream<Arguments> provideSentencesWithExpectedWords() {
    return Stream.of(
        Arguments.of("Hello World Hello", Set.of("hello", "world")),
        Arguments.of("Hello123 World! Test@#$ 456", Set.of("hello", "world", "test")),
        Arguments.of("HELLO WoRLd hello WORLD", Set.of("hello", "world")),
        Arguments.of("Hello, one! Two? Three!? @$@$ One!Four", Set.of("hello", "one", "two", "three", "four"))
    );
  }

  @ParameterizedTest
  @DisplayName("Should save words from multiple following sentences")
  @MethodSource("provideMultipleSentencesWithExpectedWords")
  void shouldSaveWordsFromMultipleFollowingSentences(Stream<String> sentences, Set<String> expectedWords) {
    // when
    sentences.forEach(s -> wordsService.saveWords(s));

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(expectedWords.size(), words.size());
    assertTrue(words.containsAll(expectedWords));
  }

  private static Stream<Arguments> provideMultipleSentencesWithExpectedWords() {
    return Stream.of(
        Arguments.of(Stream.of("Hello World", "Java Programming", "Spring Boot is Cool"),
            Set.of("hello", "world", "java", "programming", "spring", "boot", "is", "cool")),
        Arguments.of(Stream.of("Hello World", "Hello Java"), Set.of("hello", "world", "java")),
        Arguments.of(Stream.of("First sentence", "Second sentence", "Third sentence", "Fourth sentence"),
            Set.of("first", "second", "third", "fourth", "sentence"))
        );
  }
}
