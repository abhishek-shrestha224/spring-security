package com.example.bookstore.controller;

import com.example.bookstore.dto.req.SigninRequest;
import com.example.bookstore.dto.req.SignupRequest;
import com.example.bookstore.dto.res.JwtAuthnResponse;
import com.example.bookstore.dto.res.UserDto;
import com.example.bookstore.service.AuthnService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@SuppressWarnings("unused")
public class AuthController {
  private final AuthnService authnService;

  @PostMapping("signup")
  @ResponseStatus(HttpStatus.CREATED)
  public UserDto signup(@RequestBody @Valid final SignupRequest request) {
    return authnService.signup(request);
  }

  @PostMapping("signin")
  @ResponseStatus(HttpStatus.CREATED)
  public JwtAuthnResponse signin(@RequestBody @Valid final SigninRequest request) {
    return authnService.signin(request);
  }

  @PostMapping("token-refresh")
  public String refreshToken(@AuthenticationPrincipal UserDetails userDetails) {
    return authnService.refreshToken(userDetails);
  }

  @PostMapping("signout")
  public String signout(@AuthenticationPrincipal UserDetails userDetails) {
    return authnService.signout(userDetails);
  }
}
