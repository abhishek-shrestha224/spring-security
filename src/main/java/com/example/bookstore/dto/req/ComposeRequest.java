package com.example.bookstore.dto.req;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import java.util.Set;

public record ComposeRequest(
    @NotEmpty(message = "At least one permission is required.") Set<String> permissions,
    @Nullable Boolean decompose) {

  public ComposeRequest {
    if (decompose == null) {
      decompose = false;
    }
  }
}
