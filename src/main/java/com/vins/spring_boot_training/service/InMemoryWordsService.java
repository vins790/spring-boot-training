package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.service.interfaces.WordsService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Profile("in-memory")
public class InMemoryWordsService implements WordsService {

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
