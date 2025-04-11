package com.example.bookstore.service.utils;

import com.example.bookstore.dto.res.UserDto;
import com.example.bookstore.dto.req.SignupRequest;
import com.example.bookstore.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Slf4j
public class UserMapper {

  public User userEntity(SignupRequest request) {

    if (null == request) {
      log.warn("Tried mapping null SignupRequest to User entity.");
      return null;
    }

    return User.builder()
        .id(null)
        .firstName(request.firstName())
        .lastName(request.lastName())
        .age(request.age())
        .username(request.username())
        .password(request.password())
        .build();
  }

  public UserDto userDto(User entity) {

    if (null == entity) {
      log.warn("Tried mapping null User entity to UserDto.");
      return null;
    }
    return UserDto.builder()
        .firstName(entity.getFirstName())
        .lastName(entity.getLastName())
        .age(entity.getAge())
        .username(entity.getUsername())
        .password(entity.getPassword())
        .roles(entity.getAuthorities().stream().map(Object::toString).collect(Collectors.toSet()))
        .build();
  }
}
