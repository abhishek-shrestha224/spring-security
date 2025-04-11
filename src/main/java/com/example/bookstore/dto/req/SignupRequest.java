package com.example.bookstore.dto.req;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record SignupRequest(
    @NotBlank(message = "Fist name is required.") String firstName,
    @NotBlank(message = "Last name is required.") String lastName,
    @NotNull(message = "Age is required.") @Positive(message = "Age must be positive.") Integer age,
    @NotBlank(message = "Username is required.")
        @Size(min = 4, max = 8, message = "Username should be between 4 and 8 characters.")
        String username,
    @NotBlank(message = "Password is required.")
        @Size(min = 8, message = "Password should have at least 8 characters.")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        String password) {}
