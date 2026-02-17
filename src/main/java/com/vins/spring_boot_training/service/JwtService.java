package com.vins.spring_boot_training.service;

import com.vins.spring_boot_training.config.SecurityConfig;
import com.vins.spring_boot_training.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
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

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    isTokenExpired(token);
    return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
  }

  public String generateToken(Map<String, Object> claims, UserDetails userDetails) {
    return Jwts.builder()
        .claims(claims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + securityConfig.getJwtExpiration()))
        .signWith(getSigningKey(), Jwts.SIG.HS256).compact();
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
}
