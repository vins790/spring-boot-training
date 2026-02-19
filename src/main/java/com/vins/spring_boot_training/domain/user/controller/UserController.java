package com.vins.spring_boot_training.domain.user.controller;

import com.vins.spring_boot_training.domain.user.service.UserService;
import com.vins.spring_boot_training.domain.user.dto.UserInfoDto;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {
  private final UserService userService;

  @GetMapping("/info")
  public UserInfoDto getUser() {
    return userService.getUserInfo();
  }
}
