package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.entity.Word;
import com.vins.spring_boot_training.repository.WordsRepository;
import com.vins.spring_boot_training.service.interfaces.WordsService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile({"mysql", "h2"})
public class PersistentWordsService implements WordsService {
  private final WordsRepository repository;

  public PersistentWordsService(WordsRepository repository) {
    this.repository = repository;
  }

  @Override
  public void saveWords(String sentence) {
    Set<String> wordsToSave = this.extractWords(sentence);
    wordsToSave.forEach(word -> {
      if (repository.findByWord(word).isEmpty()) {
        Word newWord = new Word(word);
        repository.save(newWord);
      }
    });
  }

  @Override
  public Set<String> getWords() {
    return repository.findAll().stream()
        .map(Word::getWord)
        .collect(Collectors.toSet());
  }
}
