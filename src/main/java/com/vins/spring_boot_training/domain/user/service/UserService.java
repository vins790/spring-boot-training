package com.vins.spring_boot_training.domain.user.service;

import com.vins.spring_boot_training.domain.user.dto.UserInfoDto;
import com.vins.spring_boot_training.domain.user.entity.User;
import com.vins.spring_boot_training.domain.word.entity.Word;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.UserErrors;
import com.vins.spring_boot_training.domain.user.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class UserService implements UserDetailsService {

  private final UsersRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws CustomException {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME))
        .toUserDetails();
  }

  @Transactional(readOnly = true)
  public UserInfoDto getUserInfo() throws CustomException {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new AccessDeniedException("Access Denied");
    }
    String username = authentication.getName();
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME));
    return new UserInfoDto(user.getUsername(),
        user.getWords()
            .stream()
            .map(Word::getWord)
            .collect(java.util.stream.Collectors.toSet()));
  }

  public Long getIdByUserDetails(UserDetails userDetails) throws CustomException {
    return userRepository
        .findByUsername(userDetails.getUsername())
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME))
        .getId();
  }

  public Set<User> getAllUsers() {
    return new HashSet<>(userRepository.findAll());
  }

  public Set<Long> getAllUsersIds() {
    return getAllUsers()
        .stream()
        .map(User::getId)
        .collect(Collectors.toSet());
  }
}
