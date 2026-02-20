package com.vins.spring_boot_training.domain.word.controller;

import com.vins.spring_boot_training.domain.word.dto.WordFrequencyDto;
import com.vins.spring_boot_training.domain.word.service.WordFrequencyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/frequency")
public class WordFrequencyController {

  private WordFrequencyService wordFrequencyService;

  @GetMapping("/word-frequency/{language}/{word}")
  public WordFrequencyDto getWordFrequency(
      @PathVariable String language,
      @PathVariable String word) {
    return wordFrequencyService.getWordFrequency(word, language);
  }
}
