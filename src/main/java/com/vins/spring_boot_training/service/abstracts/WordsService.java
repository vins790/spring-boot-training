package com.vins.spring_boot_training.service.abstracts;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class WordsService {
  protected Set<String> extractWords(String sentence) {
    if (sentence == null || sentence.isEmpty()) {
      return java.util.Collections.emptySet();
    }

    return Arrays.stream(sentence.split("[^a-zA-Z]+"))
        .filter(word -> !word.isEmpty())
        .map(String::toLowerCase)
        .collect(Collectors.toSet());
  }

  abstract public void saveWords(String sentence);
  abstract public Set<String> getWords();

}
