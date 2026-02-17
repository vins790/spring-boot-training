package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.dto.UserCredentialsDto;
import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.UserErrors;
import com.vins.spring_boot_training.repository.UsersRepository;
import com.vins.spring_boot_training.dto.TokenDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class AuthenticationService {

  private final UsersRepository userRepository;
  private final UserService userService;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  public void registerUser(UserCredentialsDto userDto) {
    if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
      throw new CustomException(UserErrors.USER_ALREADY_EXISTS);
    }

    String encodedPassword = passwordEncoder.encode(userDto.getPassword());
    User newUser = new User(userDto.getUsername(), encodedPassword);
    userRepository.save(newUser);
  }

  public TokenDto login(UserCredentialsDto credentials) throws CustomException {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
    );
    UserDetails user = userService.loadUserByUsername(credentials.getUsername());
    String jwtToken = jwtService.generateToken(new HashMap<>(), user);

    return new TokenDto(jwtToken);
  }
}
