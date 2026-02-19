package com.vins.spring_boot_training.contollers;

import com.vins.spring_boot_training.dto.WordFrequencyDto;
import com.vins.spring_boot_training.service.WordFrequencyService;
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
