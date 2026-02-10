package com.vins.spring_boot_training.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryWordsServiceTest {

  private InMemoryWordsService wordsService;

  @BeforeEach
  void setUp() {
    wordsService = new InMemoryWordsService();
  }

  @Test
  @DisplayName("Should save unique words from single sentence")
  void shouldSaveUniqueWordsFromSingleSentence() {
    // given
    String sentence = "Hello World Hello";

    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(2, words.size());
    assertTrue(words.contains("hello"));
    assertTrue(words.contains("world"));
  }

  @Test
  @DisplayName("Should save words from multiple sentences")
  void shouldSaveWordsFromMultipleSentences() {
    // given
    String sentence1 = "Hello World";
    String sentence2 = "Java Programming";

    // when
    wordsService.saveWords(sentence1);
    wordsService.saveWords(sentence2);

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(4, words.size());
    assertTrue(words.contains("hello"));
    assertTrue(words.contains("world"));
    assertTrue(words.contains("java"));
    assertTrue(words.contains("programming"));
  }

  @Test
  @DisplayName("Should not duplicate words from multiple sentences")
  void shouldNotDuplicateWordsFromMultipleSentences() {
    // given
    String sentence1 = "Hello World";
    String sentence2 = "Hello Java";

    // when
    wordsService.saveWords(sentence1);
    wordsService.saveWords(sentence2);

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(3, words.size());
    assertTrue(words.contains("hello"));
    assertTrue(words.contains("world"));
    assertTrue(words.contains("java"));
  }

  @Test
  @DisplayName("Should ignore numbers and special characters")
  void shouldIgnoreNumbersAndSpecialCharacters() {
    // given
    String sentence = "Hello123 World! Test@#$ 456";

    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(3, words.size());
    assertTrue(words.contains("hello"));
    assertTrue(words.contains("world"));
    assertTrue(words.contains("test"));
    assertFalse(words.contains("hello123"));
    assertFalse(words.contains("123"));
  }

  @Test
  @DisplayName("Should convert all words to lowercase")
  void shouldConvertAllWordsToLowercase() {
    // given
    String sentence = "HELLO WoRLd hello WORLD";

    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(2, words.size());
    assertTrue(words.contains("hello"));
    assertTrue(words.contains("world"));
  }

  @Test
  @DisplayName("Should handle empty string")
  void shouldHandleEmptyString() {
    // given
    String sentence = "";

    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertTrue(words.isEmpty());
  }

  @Test
  @DisplayName("Should handle null string")
  void shouldHandleNullString() {
    // given
    String sentence = null;

    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertTrue(words.isEmpty());
  }

  @Test
  @DisplayName("Should handle string with only special characters")
  void shouldHandleStringWithOnlySpecialCharacters() {
    // given
    String sentence = "!@#$%^&*()_+";

    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertTrue(words.isEmpty());
  }

  @Test
  @DisplayName("Should handle string with only numbers")
  void shouldHandleStringWithOnlyNumbers() {
    // given
    String sentence = "123 456 789";

    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertTrue(words.isEmpty());
  }

  @Test
  @DisplayName("Should accumulate words across multiple calls")
  void shouldAccumulateWordsAcrossMultipleCalls() {
    // given
    String sentence1 = "First sentence";
    String sentence2 = "Second sentence";
    String sentence3 = "Third sentence";
    String sentence4 = "Fourth sentence";

    // when
    wordsService.saveWords(sentence1);
    wordsService.saveWords(sentence2);
    wordsService.saveWords(sentence3);
    wordsService.saveWords(sentence4);

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(5, words.size());
    assertTrue(words.contains("first"));
    assertTrue(words.contains("second"));
    assertTrue(words.contains("third"));
    assertTrue(words.contains("fourth"));
    assertTrue(words.contains("sentence"));
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

  @Test
  @DisplayName("Should handle complex sentence with mixed content")
  void shouldHandleComplexSentenceWithMixedContent() {
    // given
    String sentence = "Hello, one! Two? Three!? @$@$ One!Four";

    // when
    wordsService.saveWords(sentence);

    // then
    Set<String> words = wordsService.getWords();
    assertEquals(5, words.size());
    assertTrue(words.contains("hello"));
    assertTrue(words.contains("one"));
    assertTrue(words.contains("two"));
    assertTrue(words.contains("three"));
    assertTrue(words.contains("four"));
  }
}
