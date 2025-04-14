package com.example.bookstore;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            auth -> {
              auth.requestMatchers("/public", "/oauth/**", "/auth/**").permitAll();
              auth.anyRequest().authenticated();
            })
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .userDetailsService(userDetailsManager())
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  @Bean
  InMemoryUserDetailsManager userDetailsManager() {
    UserDetails testUser =
        User.builder()
            .username("abhishekshrestha416@gmail.com")
            .password("{noop}abhishekshrestha416@gmail.com")
            .roles("ADMIN")
            .build();
    return new InMemoryUserDetailsManager(testUser);
  }
}