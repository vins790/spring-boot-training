package com.vins.spring_boot_training.domain.user.service;

import com.vins.spring_boot_training.domain.user.entity.User;
import com.vins.spring_boot_training.domain.user.enums.UserRole;
import com.vins.spring_boot_training.domain.user.repository.UsersRepository;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.UserErrors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserRolesService {

  private final UsersRepository usersRepository;

  public void addRole(String username, UserRole role) throws CustomException {
    User user = usersRepository
        .findByUsername(username)
        .orElseThrow(() -> new CustomException(UserErrors.USER_INVALID_USERNAME));

      if(user.hasRole(role)) throw new CustomException(UserErrors.USER_ROLE_ALREADY_ADDED);
      user.addRole(role);
      usersRepository.save(user);
  }
}
