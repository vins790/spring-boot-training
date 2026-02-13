package com.vins.spring_boot_training.config;

import com.vins.spring_boot_training.entity.User;
import com.vins.spring_boot_training.service.JwtService;
import com.vins.spring_boot_training.service.interfaces.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwtService;
  private final UsersService usersService;

  public JwtAuthenticationFilter(JwtService jwtService, @Lazy UsersService usersService) {
    this.jwtService = jwtService;
    this.usersService = usersService;
  }

  @Override
  protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response,
                                  @NotNull FilterChain filterChain)
      throws ServletException, IOException {
    final String authorizationHeader = request.getHeader("Authorization");
    final String jwt;
    final String username;

    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      filterChain.doFilter(request, response);
      return;
    }

    jwt = authorizationHeader.substring(7);
    username = jwtService.extractUsername(jwt);

    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      User user = usersService.loadUserByUsername(username);

      if (jwtService.isTokenValid(jwt, user)) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user, null);
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
      }
    }
  }
}
