package com.vins.spring_boot_training.domain.user.entity;

import com.vins.spring_boot_training.domain.user.enums.UserRole;
import com.vins.spring_boot_training.domain.word.entity.Word;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import static org.springframework.security.core.userdetails.User.withUsername;

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

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  @Column(name = "role")
  private Set<UserRole> roles = new HashSet<>();

  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.words = new HashSet<>();
    this.roles = new HashSet<>();
    addRole(UserRole.USER);
  }

  public UserDetails toUserDetails() {
    List<SimpleGrantedAuthority> authorities = roles
        .stream()
        .map(UserRole::getAuthority)
        .map(SimpleGrantedAuthority::new)
        .toList();

    return withUsername(this.username)
        .password(this.password)
        .authorities(authorities)
        .build();
  }

  public void addRole(UserRole role) {
    this.roles.add(role);
  }

  public void removeRole(UserRole role) {
    this.roles.remove(role);
  }

  public boolean hasRole(UserRole role) {
    return this.roles.contains(role);
  }
}
