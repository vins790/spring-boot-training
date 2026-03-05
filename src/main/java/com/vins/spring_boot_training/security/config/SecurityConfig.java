package com.vins.spring_boot_training.security.config;

import com.vins.spring_boot_training.domain.user.enums.UserRole;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Getter
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Value("${spring.jwt.secret}")
  private String jwtSecret;

  @Value("${spring.jwt.access.expiration}")
  private long jwtAccessExpiration;

  @Value("${spring.jwt.refresh.expiration}")
  private long jwtRefreshExpiration;

  @Value("${frontend.url}")
  private String frontendUrl;

  @Value("${frontend.port}")
  private String frontendPort;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    String allowedOrigin = frontendUrl + ":" + frontendPort;
    return (request, response, authException) -> {
      response.setHeader("Access-Control-Allow-Origin", allowedOrigin);
      response.setHeader("Access-Control-Allow-Credentials", "true");
      response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
      response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Cookie");
      response.setHeader("Access-Control-Expose-Headers", "Authorization, Set-Cookie");

      response.setStatus(HttpStatus.UNAUTHORIZED.value());
      response.setContentType("application/json");
      response.setHeader("WWW-Authenticate", "");
      response.getWriter().write("{\"error\": \"Unauthorized\", \"message\": \"" + authException.getMessage() + "\"}");
    };
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) {
    http.authorizeHttpRequests(configurer ->
        configurer
            .requestMatchers(HttpMethod.OPTIONS).permitAll() // Allow OPTIONS requests without authentication
            .requestMatchers(
                "/api/auth/**",
                "/docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/webjars/**",
                "/swagger-resources/**"
            ).permitAll()
            .requestMatchers(
                "/api/admin/**",
                "/api/archive/**"
            ).hasAuthority(UserRole.ADMIN.getAuthority())
            .anyRequest().authenticated()
    );

    http.csrf(AbstractHttpConfigurer::disable);
    http.exceptionHandling(exceptionHandling -> exceptionHandling.authenticationEntryPoint(authenticationEntryPoint()));
    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
