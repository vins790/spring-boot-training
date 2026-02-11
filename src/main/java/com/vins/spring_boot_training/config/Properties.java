package com.vins.spring_boot_training.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Properties {

  @Value("${frontend.url}")
  private String frontendUrl;

  @Value("${frontend.port}")
  private int frontendPort;

  public String getFrontendUrl() {
    return frontendUrl;
  }

  public int getFrontendPort() {
    return frontendPort;
  }
}
