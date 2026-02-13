package com.vins.spring_boot_training.contollers;

import com.vins.spring_boot_training.dto.UserCredentialsDto;
import com.vins.spring_boot_training.service.PersistentUserService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UsersController {

  private final PersistentUserService service;

  public UsersController(PersistentUserService service) {
    this.service = service;
  }

  @PostMapping
  public void addUser(@RequestBody UserCredentialsDto credentials) {
    service.registerUser(credentials);
  }
}
