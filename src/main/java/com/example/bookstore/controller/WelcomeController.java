package com.example.bookstore.controller;

import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WelcomeController {
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @SuppressWarnings("unused")
  public Map<String, String> hello() {
    return Map.of("Hello", "World");
  }
}
