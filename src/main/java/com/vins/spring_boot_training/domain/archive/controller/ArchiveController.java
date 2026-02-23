package com.vins.spring_boot_training.domain.archive.controller;

import com.vins.spring_boot_training.domain.archive.service.ArchiveService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/archive")
@AllArgsConstructor
public class ArchiveController {

  private final ArchiveService archiveService;

  @PostMapping()
  public void archive() {
    archiveService.archive();
  }
}
