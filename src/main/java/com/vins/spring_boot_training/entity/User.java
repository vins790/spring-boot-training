package com.vins.spring_boot_training.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_id", nullable = false)
  long id;

  @Column(unique = true, nullable = false)
  String username;

  @Column(nullable = false)
  String password;

  @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Set<Word> words = new HashSet<>();

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.words = new HashSet<>();
  }
}
