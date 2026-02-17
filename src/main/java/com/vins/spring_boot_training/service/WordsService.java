package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.entity.Sentence;
import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.entity.Word;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.UserErrors;
import com.vins.spring_boot_training.repository.SentencesRepository;
import com.vins.spring_boot_training.repository.UsersRepository;
import com.vins.spring_boot_training.repository.WordsRepository;
import jakarta.transaction.Transactional;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

@Service
@Profile({"mysql", "h2"})
public class WordsService {
  private final WordsRepository wordsRepository;
  private final UsersRepository usersRepository;
  private final SentencesRepository sentencesRepository;

  private Set<String> extractWords(String sentence) {
    if (sentence == null || sentence.isEmpty()) {
      return emptySet();
    }

    return Arrays.stream(sentence.split("[^a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+"))
        .filter(word -> !word.isEmpty())
        .map(String::toLowerCase)
        .collect(Collectors.toSet());
  }

  public WordsService(WordsRepository wordsRepository,
                      UsersRepository usersRepository,
                      SentencesRepository sentencesRepository) {
    this.wordsRepository = wordsRepository;
    this.usersRepository = usersRepository;
    this.sentencesRepository = sentencesRepository;
  }

  @Transactional
  public void saveWords(String sentence, Long userId) {
    User user = usersRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME));

    Sentence sentenceEntity = new Sentence(sentence, user);
    sentencesRepository.save(sentenceEntity);

    Set<String> wordsToSave = this.extractWords(sentence);
    wordsToSave.forEach(word -> {
      Word wordEntity = wordsRepository.findByWord(word)
          .orElseGet(() -> wordsRepository.save(new Word(word, sentenceEntity)));
      user.getWords().add(wordEntity);
    });
    usersRepository.save(user);
  }

  public Set<String> getWords() {
    return wordsRepository.findAll()
        .stream()
        .map(Word::getWord)
        .collect(Collectors.toSet());
  }

  @Transactional
  public Set<String> getWords(Long userId) {
    User user = usersRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME));

    return user.getWords()
        .stream()
        .map(Word::getWord)
        .collect(Collectors.toSet());
  }
}
