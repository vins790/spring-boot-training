package com.vins.spring_boot_training.contollers;

import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.service.UserService;
import com.vins.spring_boot_training.service.WordsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/words")
public class WordsController {

  private final WordsService wordsService;
  private final UserService userService;

  public WordsController(WordsService wordsService, UserService userService) {
    this.wordsService = wordsService;
    this.userService = userService;
  }

  @GetMapping("/all")
  public Set<String> getWords() {
    return wordsService.getWords();
  }

  @GetMapping()
  public Set<String> getWords(@AuthenticationPrincipal UserDetails user) {
    return wordsService.getWords(
        userService.getIdByUserDetails(user)
    );
  }


  @PostMapping
  public void addWord(@RequestBody String sentence, @AuthenticationPrincipal UserDetails user)
      throws CustomException {
    wordsService.saveWords(
        sentence,
        userService.getIdByUserDetails(user)
    );
  }
}
