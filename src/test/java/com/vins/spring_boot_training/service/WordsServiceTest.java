package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.entity.Word;
import com.vins.spring_boot_training.exception.UserInvalidUsernameException;
import com.vins.spring_boot_training.repository.UsersRepository;
import com.vins.spring_boot_training.repository.WordsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordsServiceTest {

  @Mock
  private WordsRepository wordsRepository;

  @Mock
  private UsersRepository usersRepository;

  @InjectMocks
  private WordsService wordsService;

  private User user;

  @BeforeEach
  void setUp() {
    user = new User("john", "pwd");
    user.setId(1L);
  }

  @Test
  void shouldSaveNewWordsAndLinkToUser() {
    when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
    when(wordsRepository.findByWord(anyString())).thenReturn(Optional.empty());
    when(wordsRepository.save(any(Word.class))).thenAnswer(invocation -> invocation.getArgument(0));

    wordsService.saveWords("Hello World", 1L);

    assertEquals(2, user.getWords().size());
    verify(wordsRepository, times(2)).save(any(Word.class));
    verify(usersRepository).save(user);
  }

  @Test
  void shouldReuseExistingWordsWhenAlreadyPersisted() {
    Word hello = new Word("hello");
    when(usersRepository.findById(1L)).thenReturn(Optional.of(user));
    when(wordsRepository.findByWord("hello")).thenReturn(Optional.of(hello));
    when(wordsRepository.findByWord("world")).thenReturn(Optional.empty());
    when(wordsRepository.save(any(Word.class))).thenAnswer(invocation -> invocation.getArgument(0));

    wordsService.saveWords("Hello World", 1L);

    assertTrue(user.getWords().contains(hello));
    verify(wordsRepository, times(1)).save(argThat(word -> word.getWord().equals("world")));
    verify(wordsRepository, never()).save(argThat(word -> word.getWord().equals("hello")));
  }

  @Test
  void shouldIgnoreSentencesWithoutWords() {
    when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

    wordsService.saveWords("1234 $$$", 1L);

    assertTrue(user.getWords().isEmpty());
    verify(wordsRepository, never()).save(any());
    verify(usersRepository).save(user);
  }

  @Test
  void shouldThrowWhenUserNotFoundOnSave() {
    when(usersRepository.findById(99L)).thenReturn(Optional.empty());

    assertThrows(UserInvalidUsernameException.class,
        () -> wordsService.saveWords("hello", 99L));
  }

  @Test
  void shouldReturnAllWords() {
    when(wordsRepository.findAll()).thenReturn(List.of(new Word("hello"), new Word("world")));

    Set<String> result = wordsService.getWords();

    assertEquals(Set.of("hello", "world"), result);
  }

  @Test
  void shouldReturnWordsForUser() {
    Word hello = new Word("hello");
    user.getWords().add(hello);
    when(usersRepository.findById(1L)).thenReturn(Optional.of(user));

    Set<String> result = wordsService.getWords(1L);

    assertEquals(Set.of("hello"), result);
  }
}
