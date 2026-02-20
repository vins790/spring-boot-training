package com.vins.spring_boot_training.domain.score.entity;

import com.vins.spring_boot_training.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="score")
@Getter
@Setter
@NoArgsConstructor
public class Score {

  @Id
  @Column(name = "user_id", unique = true, nullable = false)
  private Long userId;

  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "user_id")
  private User user;

  @Column(nullable = false)
  private Long score;

  public Score(User user, Long score) {
    this.user = user;
    this.score = score;
  }
}
