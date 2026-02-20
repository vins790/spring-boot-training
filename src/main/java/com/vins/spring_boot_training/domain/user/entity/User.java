package com.vins.spring_boot_training.domain.user.entity;

import com.vins.spring_boot_training.domain.word.entity.Word;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.HashSet;
import java.util.List;
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

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true )
  @JoinTable(
      name = "user_words",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "word_id")
  )
  private Set<Word> words;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.words = new HashSet<>();
  }

  public UserDetails toUserDetails() {
    return new org.springframework.security.core.userdetails.User(
        this.username,
        this.password,
        List.of()
    );
  }
}
