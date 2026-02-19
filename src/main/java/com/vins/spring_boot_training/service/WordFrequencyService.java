package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.config.Properties;
import com.vins.spring_boot_training.dto.WordFrequencyDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class WordFrequencyService {
  private final Properties properties;
  private final RestTemplate restTemplate;

  public WordFrequencyDto getWordFrequency(String word, String language) {
    String url = String.format("%s/freq?word=%s&lang=%s",
        properties.getWordFrequencyServiceUrl(), word, language);
    return restTemplate.getForObject(url, WordFrequencyDto.class);
  }
}