package com.vins.spring_boot_training.domain.user.controller;

import com.vins.spring_boot_training.domain.user.enums.UserRole;
import com.vins.spring_boot_training.domain.user.service.UserRolesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/api/admin")
public class AdminController {

  private final UserRolesService userRolesService;

  @PostMapping("/add-role")
  public void addRole(@RequestParam String username,
                      @RequestParam UserRole role) {
    userRolesService.addRole(username, role);
  }
}
