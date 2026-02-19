package com.vins.spring_boot_training.domain.user.repository;

import com.vins.spring_boot_training.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
