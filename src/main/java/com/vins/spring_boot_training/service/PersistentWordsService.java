package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.entity.Word;
import com.vins.spring_boot_training.repository.WordsRepository;
import com.vins.spring_boot_training.service.interfaces.WordsService;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile({"mysql", "h2"})
public class PersistentWordsService implements WordsService {
  private final WordsRepository repository;
  private final EntityManager entityManager;

  public PersistentWordsService(WordsRepository repository, EntityManager entityManager) {
    this.repository = repository;

    //TODO: zastąpić user repository
    this.entityManager = entityManager;
  }

  public void saveWords(String sentence, Long userId) {
    User userReference = entityManager.getReference(User.class, userId);

    Set<String> wordsToSave = this.extractWords(sentence);
    wordsToSave.forEach(word -> {
      if (repository.findByWord(word).isEmpty()) {
        Word newWord = new Word(word, userReference);
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
