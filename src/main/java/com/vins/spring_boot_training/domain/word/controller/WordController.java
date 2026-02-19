package com.vins.spring_boot_training.domain.word.controller;

import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.domain.user.service.UserService;
import com.vins.spring_boot_training.domain.word.service.WordService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/words")
public class WordController {

  private final WordService wordService;
  private final UserService userService;

  public WordController(WordService wordService, UserService userService) {
    this.wordService = wordService;
    this.userService = userService;
  }

  @GetMapping("/all")
  public Set<String> getWords() {
    return wordService.getWords();
  }

  @GetMapping
  public Set<String> getWords(@AuthenticationPrincipal UserDetails user) {
    return wordService.getWords(
        userService.getIdByUserDetails(user)
    );
  }

  @PostMapping
  public void addWord(@RequestBody String sentence, @AuthenticationPrincipal UserDetails user)
      throws CustomException {
    wordService.saveWords(
        sentence,
        userService.getIdByUserDetails(user)
    );
  }
}
