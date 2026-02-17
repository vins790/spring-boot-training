package com.vins.spring_boot_training.repository;

import com.vins.spring_boot_training.entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User, Long> {
  Optional<User> findByUsername(String username);
}
