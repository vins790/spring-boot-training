package com.vins.spring_boot_training.entity;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "words")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Word {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name="word_id", nullable = false)
  private long id;

  @Column(unique = true, nullable = false)
  private String word;

  @Timestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinTable(
      name = "user_words",
      joinColumns = @JoinColumn(name = "word_id"),
      inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private User user;

  public Word(String word, User user) {
    this.word = word;
    this.user = user;
  }
}
