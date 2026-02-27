package com.vins.spring_boot_training.security.service;

import com.vins.spring_boot_training.domain.user.enums.UserRole;
import com.vins.spring_boot_training.security.config.SecurityConfig;
import com.vins.spring_boot_training.security.dto.TokenSetDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private final SecurityConfig securityConfig;

  public JwtService(SecurityConfig securityConfig) {
    this.securityConfig = securityConfig;
  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public String extractExpiration(String token) { return extractClaim(token, claims -> claims.get("role", String.class)); }

  public ResponseCookie generateRefreshTokenCookie(String token) {
    return ResponseCookie.from("refreshToken", token)
        .httpOnly(true)
        .secure(false)
        .path("/api/auth/refresh")
        .maxAge(7 * 24 * 60 * 60) // 7 days
        .sameSite("Lax")
        .build();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    isTokenExpired(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public TokenSetDto generateTokenSet(UserDetails userDetails) {
    return new TokenSetDto(generateAccessToken(userDetails), generateRefreshToken(userDetails));
  }

  public String generateAccessToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("role", extractRoleFromUserDetails(userDetails));
    return createToken(claims, userDetails, securityConfig.getJwtAccessExpiration()); // 1 hour expiration
  }

  public String generateRefreshToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    return createToken(claims, userDetails, securityConfig.getJwtRefreshExpiration()); // 7 days expiration
  }

  private String createToken(Map<String, Object> claims, UserDetails userDetails, long expirationTime) {
    return Jwts.builder()
            .claims(claims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis() + expirationTime))
            .signWith(getSigningKey(), Jwts.SIG.HS256)
            .compact();
}

  private boolean isTokenExpired(String token) {
    return extractClaim(token, Claims::getExpiration).before(new Date());
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(securityConfig.getJwtSecret());
    return Keys.hmacShaKeyFor(keyBytes);
  }

  private Claims extractAllClaims(String token) {
    return Jwts.parser()
        .verifyWith(getSigningKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private String extractRoleFromUserDetails(UserDetails userDetails) {
    return userDetails.getAuthorities().stream()
        .findFirst()
        .map(GrantedAuthority::getAuthority)
        .orElse(UserRole.USER.getAuthority());
  }
}
