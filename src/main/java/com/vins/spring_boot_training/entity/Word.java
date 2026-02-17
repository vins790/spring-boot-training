package com.vins.spring_boot_training.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

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

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sentence_id", nullable = false)
  private Sentence sentence;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  public Word(String word, Sentence sentence) {
    this.word = word;
    this.sentence = sentence;
  }
}
