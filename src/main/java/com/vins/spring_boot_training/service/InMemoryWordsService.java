package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.service.abstracts.WordsService;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class InMemoryWordsService extends WordsService {

  private final Set<String> words;

  public InMemoryWordsService() {
    this.words = new HashSet<>();
  }

  @Override
  public void saveWords(String sentence) {
    Set<String> wordsToSave = this.extractWords(sentence);
    words.addAll(wordsToSave);
  }

  @Override
  public Set<String> getWords() {
    return words;
  }
}
