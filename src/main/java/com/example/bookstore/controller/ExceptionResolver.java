package com.example.bookstore.controller;

import java.util.HashMap;
import java.util.Map;

import com.example.bookstore.exceptions.AuthnFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ExceptionResolver {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public Map<String, String> handle(MethodArgumentNotValidException ex) {
    Map<String, String> errMap = new HashMap<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(err -> errMap.put(err.getField(), err.getDefaultMessage()));

    return errMap;
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handle(MethodArgumentTypeMismatchException ex) {
    return "Argument type mismatch: " + ex.getName() + " expected type: " + ex.getRequiredType();
  }

  @ExceptionHandler
  @ResponseStatus(HttpStatus.UNAUTHORIZED)
  public String handle(AuthnFailedException ex) {
    return ex.getMessage();
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public String handle(Exception ex) {
    return ex.getMessage();
  }
}
