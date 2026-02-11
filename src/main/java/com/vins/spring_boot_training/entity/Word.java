package com.vins.spring_boot_training.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "words")
public class Word {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String word;

  public Word() {
  }

  public Word(String word) {
    this.word = word;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getWord() {
    return word;
  }

  public void setWord(String word) {
    this.word = word;
  }
}
