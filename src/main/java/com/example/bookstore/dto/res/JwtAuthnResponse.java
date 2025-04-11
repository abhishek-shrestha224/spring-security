package com.example.bookstore.dto.res;

public record JwtAuthnResponse(UserDto principal, String ref, String acc) {}
