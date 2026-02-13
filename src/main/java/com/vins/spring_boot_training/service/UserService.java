package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.dto.UserInfoDto;
import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.exception.UserInvalidUsernameException;
import com.vins.spring_boot_training.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

  private final UsersRepository userRepository;

  @Override
  public User loadUserByUsername(String username) throws UserInvalidUsernameException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new UserInvalidUsernameException("User with username " + username + " not found"));
  }

  @Transactional(readOnly = true)
  public UserInfoDto getUserInfo() throws UserInvalidUsernameException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AccessDeniedException("Access Denied");
    }
    String username = authentication.getName();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UserInvalidUsernameException("User with username " + username + " not found"));
    return new UserInfoDto(user.getUsername(), user.getWords());
  }
}
