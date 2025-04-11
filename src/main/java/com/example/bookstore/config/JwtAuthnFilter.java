package com.example.bookstore.config;

import com.example.bookstore.exceptions.AuthnFailedException;
import com.example.bookstore.service.JwtService;
import com.example.bookstore.service.SessionService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Stream;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthnFilter extends OncePerRequestFilter {

  @Value("${application.security.jwt.token.refreshEndpoint}")
  private String REFRESH_ENDPOINT;

  private final UserDetailsService userDetailsService;
  private final JwtService jwtService;
  private final AuthenticationEntryPoint authnEntryPoint;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest req,
      @NonNull HttpServletResponse res,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    final String authHeader = req.getHeader("Authorization");

    if (Stream.of(ApplicationConfig.WHITE_LIST_URL)
        .map(JwtAuthnFilter::convertToRegex)
        .anyMatch(pattern -> req.getRequestURI().matches(pattern))) {
      filterChain.doFilter(req, res);
      return;
    }

    if (null == authHeader || !authHeader.startsWith("Bearer ")) {
      SecurityContextHolder.clearContext();
      authnEntryPoint.commence(req, res, new AuthnFailedException("Bearer is required."));
      return;
    }

    final String jwt = authHeader.substring(7);

    if (null != SecurityContextHolder.getContext().getAuthentication()) {
      filterChain.doFilter(req, res);
      return;
    }

    if (jwtService.isExpired(jwt)) {
      SecurityContextHolder.clearContext();
      authnEntryPoint.commence(req, res, new AuthnFailedException("Token has already expired."));
      return;
    }

    if (req.getRequestURI().startsWith((REFRESH_ENDPOINT))) {
      if (jwtService.isTokenMismatch(jwt, true)) {
        SecurityContextHolder.clearContext();
        authnEntryPoint.commence(req, res, new AuthnFailedException("Refresh token is required."));
        return;
      }
    } else {
      if (jwtService.isTokenMismatch(jwt, false)) {
        SecurityContextHolder.clearContext();
        authnEntryPoint.commence(req, res, new AuthnFailedException("Access token is required."));
        return;
      }
    }

    final String username = jwtService.extractClaims(jwt, Claims::getSubject);
    if (username.isBlank()) {
      SecurityContextHolder.clearContext();
      authnEntryPoint.commence(
          req, res, new AuthnFailedException("Token does not contain subject claim."));
      return;
    }
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (jwtService.isSubjectInvalid(jwt, userDetails)) {
      SecurityContextHolder.clearContext();
      authnEntryPoint.commence(
          req, res, new AuthnFailedException("Invalid subject claims in the token."));
      return;
    }

    UsernamePasswordAuthenticationToken authToken =
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
    SecurityContextHolder.getContext().setAuthentication(authToken);
    filterChain.doFilter(req, res);
  }

  public static String convertToRegex(String path) {
    String regex = path.replace(".", "\\.").replace("**", ".*").replace("*", "[^/]*");
    return "^" + regex + "$";
  }
}
