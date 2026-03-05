package com.vins.spring_boot_training.security.service;

import com.vins.spring_boot_training.security.dto.TokenDto;
import com.vins.spring_boot_training.security.dto.TokenSetDto;
import com.vins.spring_boot_training.security.dto.UserCredentialsDto;
import com.vins.spring_boot_training.domain.user.entity.User;
import com.vins.spring_boot_training.exception.CustomException;
import com.vins.spring_boot_training.exception.errors.UserErrors;
import com.vins.spring_boot_training.domain.user.repository.UsersRepository;
import com.vins.spring_boot_training.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

  public TokenSetDto login(UserCredentialsDto credentials) throws CustomException {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
    );
    UserDetails user = userService.loadUserByUsername(credentials.getUsername());
    return jwtService.generateTokenSet(user);
  }

  public TokenDto refresh(String refreshToken) throws CustomException {
    if (refreshToken == null || refreshToken.isEmpty()) {
      throw new CustomException(UserErrors.INVALID_TOKEN);
    }

    String username = jwtService.extractUsername(refreshToken);
    UserDetails userDetails = userService.loadUserByUsername(username);

    if (!jwtService.isTokenValid(refreshToken, userDetails)) {
      throw new CustomException(UserErrors.INVALID_TOKEN);
    }

    String newAccessToken = jwtService.generateAccessToken(userDetails);
    return new TokenDto(newAccessToken);
  }
}
