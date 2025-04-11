package com.example.bookstore.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthnFailedException extends AuthenticationException {
  public AuthnFailedException(String explanation) {
    super(explanation);
  }
}
