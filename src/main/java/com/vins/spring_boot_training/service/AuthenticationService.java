package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.dto.UserCredentialsDto;
import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.exception.UserAlreadyExistsException;
import com.vins.spring_boot_training.exception.UserInvalidUsernameException;
import com.vins.spring_boot_training.repository.UsersRepository;
import com.vins.spring_boot_training.dto.TokenDto;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;

@Service
@AllArgsConstructor
public class AuthenticationService {

  private final UsersRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public void registerUser(UserCredentialsDto userDto) {
    if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
      throw new UserAlreadyExistsException("User with username " + userDto.getUsername() + " already exists");
    }

    String encodedPassword = passwordEncoder.encode(userDto.getPassword());
    User newUser = new User(userDto.getUsername(), encodedPassword);
    userRepository.save(newUser);
  }

  @Transactional
  public TokenDto login(UserCredentialsDto credentials) throws UserInvalidUsernameException {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
    );
    User user = userRepository.findByUsername(credentials.getUsername())
        .orElseThrow(() -> new UserInvalidUsernameException("User with username " + credentials.getUsername() + " not found"));

    String jwtToken = jwtService.generateToken(new HashMap<>(), user);

    return new TokenDto(jwtToken);
  }
}
