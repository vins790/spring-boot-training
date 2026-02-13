package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.dto.UserCredentialsDto;
import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.exception.UserAlreadyExistsException;
import com.vins.spring_boot_training.exception.UserInvalidUsernameException;
import com.vins.spring_boot_training.repository.UsersRepository;
import com.vins.spring_boot_training.service.interfaces.UsersService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PersistentUserService implements UsersService {

  private final UsersRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public PersistentUserService(UsersRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  @Transactional
  public void registerUser(UserCredentialsDto userDto) {
    if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
      throw new UserAlreadyExistsException("User with username " + userDto.getUsername() + " already exists");
    }

    String encodedPassword = passwordEncoder.encode(userDto.getPassword());
    User newUser = new User(userDto.getUsername(), encodedPassword);
    userRepository.save(newUser);
  }

  @Override
  public User loadUserByUsername(String username) throws UserInvalidUsernameException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UserInvalidUsernameException("User with username " + username + " not found"));
  }
}
