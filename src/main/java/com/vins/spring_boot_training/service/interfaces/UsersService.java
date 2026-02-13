package com.vins.spring_boot_training.service.interfaces;

import com.vins.spring_boot_training.dto.UserCredentialsDto;
import com.vins.spring_boot_training.entity.User;

public interface UsersService {

  void registerUser(UserCredentialsDto credentials);

  User loadUserByUsername(String username);
}
