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


  public Word(String word) {
    this.word = word;
    this.createdAt = Instant.now();
  }
}
