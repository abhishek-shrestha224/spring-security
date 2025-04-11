package com.example.bookstore.dto.res;

import java.util.Set;

public record RoleDto(String name, Set<String> permissions) {}
