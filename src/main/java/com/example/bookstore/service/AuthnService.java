package com.example.bookstore.service;

import com.example.bookstore.dto.req.SigninRequest;
import com.example.bookstore.dto.req.SignupRequest;
import com.example.bookstore.dto.res.JwtAuthnResponse;
import com.example.bookstore.dto.res.UserDto;
import com.example.bookstore.service.utils.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthnService {
  private final UserService userService;
  private final UserMapper userMapper;
  private final JwtService jwtService;
  private final AuthenticationManager authnManager;
  private final SessionService sessionService;

  public UserDto signup(SignupRequest request) {
    return userService.create(request);
  }

  public JwtAuthnResponse signin(SigninRequest request) {
    authnManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.username(), request.password()));

    final var user = userService.findUserEntity(request.username());
    final var ref = jwtService.generateToken(true, user);
    final var acc = jwtService.generateToken(false, user);
    sessionService.startSession(user);
    return new JwtAuthnResponse(userMapper.userDto(user), ref, acc);
  }

  public String refreshToken(UserDetails userDetails) {
    final var user = userService.findUserEntity(userDetails.getUsername());
    if (sessionService.sessionInactive(user)) throw new RuntimeException("Session has expired");
    return jwtService.generateToken(false, userDetails);
  }

  public String signout(UserDetails userDetails) {
    final var user = userService.findUserEntity(userDetails.getUsername());
    if (sessionService.sessionInactive(user)) throw new RuntimeException("Session has expired");
    sessionService.endSession(user);
    return "Logged Out";
  }
}
