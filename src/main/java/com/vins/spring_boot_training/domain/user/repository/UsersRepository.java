package com.vins.spring_boot_training.domain.user.repository;

import com.vins.spring_boot_training.domain.user.entity.User;
import com.vins.spring_boot_training.domain.user.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface UsersRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u JOIN u.roles r WHERE r = :role")
  boolean existsByRole(UserRole role);
}
