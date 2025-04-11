package com.example.bookstore.dto.res;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Set;

@Builder
public record UserDto(
    String firstName,
    String lastName,
    Integer age,
    String username,
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) String password,
    Set<String> roles) {}
