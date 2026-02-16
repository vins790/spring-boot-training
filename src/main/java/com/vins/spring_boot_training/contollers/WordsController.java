package com.vins.spring_boot_training.contollers;

import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.service.WordsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/words")
public class WordsController {

  private final WordsService service;

  public WordsController(WordsService service) {
    this.service = service;
  }

  @GetMapping("/all")
  public Set<String> getWords() {
    return service.getWords();
  }

  @GetMapping()
  public Set<String> getWords(@AuthenticationPrincipal User user) {
    return service.getWords(user.getId());
  }


  @PostMapping
  public void addWord(@RequestBody String sentence, @AuthenticationPrincipal User user) {
    service.saveWords(sentence, user.getId());
  }
}
