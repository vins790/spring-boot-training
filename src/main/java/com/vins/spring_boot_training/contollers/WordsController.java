package com.vins.spring_boot_training.contollers;

import com.vins.spring_boot_training.service.interfaces.WordsService;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/words")
public class WordsController {

  private final WordsService service;

  public WordsController(WordsService service) {
    this.service = service;
  }

  @GetMapping()
  public Set<String> getWords() {
    return service.getWords();
  }

  @PostMapping
  public void addWord(@RequestBody String sentence) {
    service.saveWords(sentence);
  }
}
