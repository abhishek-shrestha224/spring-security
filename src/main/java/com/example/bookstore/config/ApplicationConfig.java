package com.example.bookstore.config;

import com.example.bookstore.exceptions.EntityNotFoundException;
import com.example.bookstore.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

  public static final String[] WHITE_LIST_URL = {"/auth/signup", "/auth/signin"};
  private final UserRepository userRepository;

  @Bean
  public UserDetailsService userDetailsService() {
    return username ->
        userRepository
            .findByUsername(username)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
  }

  @Bean
  public AuthenticationProvider authnProvider() {
    DaoAuthenticationProvider daoAuthnProvider = new DaoAuthenticationProvider();
    daoAuthnProvider.setPasswordEncoder(bCryptPasswordEncoder());
    daoAuthnProvider.setUserDetailsService(userDetailsService());
    return daoAuthnProvider;
  }

  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder(10);
  }

  @Bean
  public AuthenticationManager authnManager(AuthenticationConfiguration authnConfig)
      throws Exception {
    return authnConfig.getAuthenticationManager();
  }

  @Bean
  public AuthenticationEntryPoint authnEntryPoint() {
    return (req, res, ex) -> {
      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      res.setContentType("text/plain");
      res.getOutputStream().println(ex.getMessage());
    };
  }
}
