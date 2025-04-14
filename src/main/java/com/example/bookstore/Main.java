package com.example.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class Main {
  @Bean
  public RestClient restClient() {
    return RestClient.create();
  }

  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}