package com.example.bookstore.config;

import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.utils.UserFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Initializer implements CommandLineRunner {
  private final UserRepository userRepository;
  private final UserFactory userFactory;

  @Override
  public void run(String... args) throws Exception {
    final var user = userFactory.superUser();
    if (userRepository.existsByUsername(user.getUsername())) return;
    userRepository.save(user);
  }
}
