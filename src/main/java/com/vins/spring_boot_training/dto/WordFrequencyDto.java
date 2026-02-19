package com.vins.spring_boot_training.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WordFrequencyDto {
  private String word;
  private String language;
  private Double frequency;
}
