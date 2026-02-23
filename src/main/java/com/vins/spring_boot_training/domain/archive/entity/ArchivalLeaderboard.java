package com.vins.spring_boot_training.domain.archive.entity;

import com.vins.spring_boot_training.domain.archive.converter.LeaderboardConverter;
import com.vins.spring_boot_training.domain.score.dto.ScoreDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "archived_scores")
@Getter
@Setter
@NoArgsConstructor
public class ArchivalLeaderboard {
  @Id
  @Column(unique = true, nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long archiveId;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private Instant timestamp;

  @Column(columnDefinition = "JSON")
  @Convert(converter = LeaderboardConverter.class)
  private List<ScoreDto> leaderboard;

  public ArchivalLeaderboard(List<ScoreDto> leaderboard) {
    this.leaderboard = leaderboard;
  }
}
