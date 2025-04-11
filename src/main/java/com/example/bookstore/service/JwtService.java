package com.example.bookstore.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

  //  !Properties from application.yaml file
  @Value("${application.security.jwt.secret}")
  private String SECRET_KEY;

  //  !expiration time for access token(1 min)
  @Value("${application.security.jwt.token.access.lifeSpan}")
  private long ACCESS_TOKEN_LIFE_SPAN;

  //  !expiration time for refresh token(7 days)
  @Value("${application.security.jwt.token.refresh.lifeSpan}")
  private long REFRESH_TOKEN_LIFE_SPAN;

  //  generate jwt token
  public String generateToken(boolean refresh, UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("authorities", userDetails.getAuthorities());
    claims.put("ref", refresh);
    return Jwts.builder()
        .claims(claims)
        .subject(userDetails.getUsername())
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(
            new Date(
                System.currentTimeMillis()
                    + (refresh ? REFRESH_TOKEN_LIFE_SPAN : ACCESS_TOKEN_LIFE_SPAN)))
        .signWith(getSigningKey())
        .compact();
  }

  //  extract claims from token
  public <T> T extractClaims(String token, Function<Claims, T> claimResolver) {
    Claims claims = getPayload(token);
    return claimResolver.apply(claims);
  }

  public boolean isExpired(String token) {
    try {
      return extractClaims(token, Claims::getExpiration).before(new Date());
    } catch (ExpiredJwtException ex) {
      throw new RuntimeException("Token is expired");
    }
  }

  public boolean isTokenMismatch(String token, boolean refresh) {
    return extractClaims(token, claim -> claim.get("ref", Boolean.class)) != refresh;
  }

  public boolean isSubjectInvalid(String token, UserDetails userDetails) {
    return !extractClaims(token, Claims::getSubject).equals(userDetails.getUsername());
  }

  //  Utils
  private Claims getPayload(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  private SecretKey getSigningKey() {
    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
