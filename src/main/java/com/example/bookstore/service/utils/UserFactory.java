package com.example.bookstore.service.utils;

import com.example.bookstore.entity.User;
import com.example.bookstore.repository.RoleRepository;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFactory {

  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  public User superUser() {
    String password = "password";
    String firstName = "SUPER";
    String lastName = "USER";
    Integer age = 200;
    String username = "admin";
    String role = "ADMIN";
    return User.builder()
        .id(null)
        .firstName(firstName)
        .lastName(lastName)
        .age(age)
        .username(username)
        .password(passwordEncoder.encode(password))
        .roles(
            Set.of(
                roleRepository
                    .findByName(role)
                    .orElseThrow(() -> new RuntimeException("Something went wrong."))))
        .build();
  }
}
