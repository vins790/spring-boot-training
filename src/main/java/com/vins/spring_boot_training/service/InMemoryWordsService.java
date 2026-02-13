package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.entity.Word;
import com.vins.spring_boot_training.service.interfaces.WordsService;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Profile("in-memory")
public class InMemoryWordsService implements WordsService {

  private final Set<String> words;
  private final EntityManager entityManager;

  public InMemoryWordsService(EntityManager entityManager) {
    this.words = new HashSet<>();
    this.entityManager = entityManager;
  }

  public void saveWords(String sentence, Long userId) {
    User userReference = entityManager.getReference(User.class, userId);

    Set<String> wordsToSave = this.extractWords(sentence).stream().peek(word -> {
      Word wordEntity = new Word(word, userReference);
      entityManager.persist(wordEntity);
    }).collect(Collectors.toSet());

    words.addAll(wordsToSave);
  }

  @Override
  public Set<String> getWords() {
    return words;
  }
}
