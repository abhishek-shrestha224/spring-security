package com.example.bookstore.controller;

import com.example.bookstore.dto.res.UserDto;
import com.example.bookstore.service.UserService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
  private final UserService userService;

  @PreAuthorize("hasAuthority('view:user')")
  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<UserDto> list() {
    return userService.findAll();
  }

  @PreAuthorize("hasAuthority('view:user')")
  @GetMapping("{username}")
  @ResponseStatus(HttpStatus.OK)
  public UserDto retrieve(@PathVariable("username") final String username) {
    return userService.findOne(username);
  }

  @PreAuthorize("#username == principal.username  or hasAuthority('compose:user')")
  @DeleteMapping("{username}")
  @ResponseStatus(HttpStatus.GONE)
  public String destroy(@PathVariable("username") final String username) {
    return userService.delete(username);
  }

  @PreAuthorize("hasAuthority('compose:user')")
  @PostMapping("{username}/compose")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public UserDto compose(
      @PathVariable("username") @NotBlank final String username,
      @RequestBody @NotEmpty(message = "At least one role is required.") Set<String> roles) {
    return userService.compose(username, roles);
  }

  @PreAuthorize("hasAuthority('delete:user')")
  @PostMapping("{username}/decompose")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public UserDto decompose(
      @PathVariable("username") @NotBlank final String username,
      @RequestBody @NotEmpty(message = "At least one role is required.") Set<String> roles) {
    return userService.decompose(username, roles);
  }
}
