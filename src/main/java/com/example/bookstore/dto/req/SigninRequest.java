package com.example.bookstore.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SigninRequest(
    @NotBlank(message = "Username is required.")
        @Size(min = 4, max = 8, message = "Username should be between 4 and 8 characters.")
        String username,
    @NotBlank(message = "Password is required.")
        @Size(min = 8, message = "Password should have at least 8 characters.")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password) {}
