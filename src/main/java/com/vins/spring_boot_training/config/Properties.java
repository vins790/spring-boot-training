package com.vins.spring_boot_training.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class Properties {

  @Value("${frontend.url}")
  private String frontendUrl;

  @Value("${frontend.port}")
  private int frontendPort;
}
