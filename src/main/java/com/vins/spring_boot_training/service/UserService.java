package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.dto.UserInfoDto;
import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.entity.Word;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.UserErrors;
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
  public User loadUserByUsername(String username) throws CustomException {
    return userRepository.findByUsername(username)
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME));
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
}
