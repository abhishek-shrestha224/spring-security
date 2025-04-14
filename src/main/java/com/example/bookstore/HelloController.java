package com.example.bookstore;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

  @GetMapping("public")
  public String hello() {
    return "You are a teapot!";
  }

  @GetMapping("secured")
  public String secured() {
    return "You are a secured teapot!";
  }
}