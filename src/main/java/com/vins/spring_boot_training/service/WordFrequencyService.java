package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.config.Properties;
import com.vins.spring_boot_training.dto.WordFrequencyDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Locale;
import java.util.stream.IntStream;

@AllArgsConstructor
@Service
public class WordFrequencyService {
  private final Properties properties;
  private final RestTemplate restTemplate;

  public WordFrequencyDto getWordFrequency(String word, String language) {
    String url = properties.getWordFrequencyServiceUrl() + "/freq?word=" + word + "&lang=" + language;
    return restTemplate.getForObject(url, WordFrequencyDto.class);
  }

  public Long calculateFrequencyModifier(Double frequency) {
    if (frequency == null || frequency == 0.0 || !Double.isFinite(frequency)) return 0L;

    String formatted = String.format(Locale.US, "%.10f", frequency);
    String[] parts = formatted.split("\\.");

    if (parts.length < 2) return 0L;

    final String decimalPart = parts[1].length() < 10
        ? String.format("%-10s", parts[1]).replace(' ', '0')
        : parts[1];

    return IntStream.range(0, 10)
        .mapToLong(i -> (long) Character.getNumericValue(decimalPart.charAt(i)) * (10 - i))
        .sum();
  }

  public Long getWordFrequencyModifier(String word, String language) {
    WordFrequencyDto wordFrequency = getWordFrequency(word, language);
    return calculateFrequencyModifier(wordFrequency.getFrequency());
  }
}