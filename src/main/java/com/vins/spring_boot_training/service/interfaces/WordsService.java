package com.vins.spring_boot_training.service.interfaces;

import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

public interface WordsService {
  default Set<String> extractWords(String sentence) {
    if (sentence == null || sentence.isEmpty()) {
      return emptySet();
    }

    return Arrays.stream(sentence.split("[^a-zA-Z]+"))
        .filter(word -> !word.isEmpty())
        .map(String::toLowerCase)
        .collect(Collectors.toSet());
  }

  void saveWords(String sentence, Long userId);

  Set<String> getWords();

}
