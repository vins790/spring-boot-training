package com.vins.spring_boot_training.domain.word.service;

import com.vins.spring_boot_training.domain.archive.service.ArchiveService;
import com.vins.spring_boot_training.domain.user.entity.User;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.UserErrors;
import com.vins.spring_boot_training.domain.user.repository.UsersRepository;
import com.vins.spring_boot_training.domain.word.entity.Sentence;
import com.vins.spring_boot_training.domain.word.entity.Word;
import com.vins.spring_boot_training.domain.word.repository.SentencesRepository;
import com.vins.spring_boot_training.domain.word.repository.WordRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;

@AllArgsConstructor
@Service
@Profile({"mysql", "h2"})
public class WordService {
  private final WordRepository wordRepository;
  private final UsersRepository usersRepository;
  private final SentencesRepository sentencesRepository;
  private final ArchiveService archiveService;

  private Set<String> extractWords(String sentence) {
    if (sentence == null || sentence.isEmpty()) {
      return emptySet();
    }

    return Arrays.stream(sentence.split("[^a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ]+"))
        .filter(word -> !word.isEmpty())
        .map(String::toLowerCase)
        .collect(Collectors.toSet());
  }

  @Transactional
  public void saveWords(String sentence, Long userId) {
    User user = usersRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME));

    Sentence sentenceEntity = new Sentence(sentence, user);
    sentencesRepository.save(sentenceEntity);

    Set<String> wordsToSave = this.extractWords(sentence);
    wordsToSave.forEach(word -> {
      Word wordEntity = wordRepository.findByWord(word)
          .orElseGet(() -> wordRepository.save(new Word(word, sentenceEntity)));
      user.getWords().add(wordEntity);
    });
    usersRepository.save(user);
  }

  public Set<String> getWords() {
    return wordRepository.findAll()
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

  @Transactional
  public Set<String> getScoringWords(Long userId) {
    User user = usersRepository.findById(userId)
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME));

    Instant lastArchiveTimestamp = archiveService.getLastArchiveTimestamp();

    return user.getWords()
        .stream()
        .filter(word -> lastArchiveTimestamp == null || word.getCreatedAt().isAfter(lastArchiveTimestamp))
        .map(Word::getWord)
        .collect(Collectors.toSet());
  }
}
