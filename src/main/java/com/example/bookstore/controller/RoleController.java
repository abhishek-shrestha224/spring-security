package com.example.bookstore.controller;

import com.example.bookstore.dto.req.ComposeRequest;
import com.example.bookstore.dto.res.RoleDto;
import com.example.bookstore.service.RoleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import java.util.Set;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("roles")
@RequiredArgsConstructor
public class RoleController {

  private final String REGEX = "^[A-Z]+(_[A-Z]+)*$";
  private final RoleService roleService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public RoleDto create(@RequestParam("role") @Pattern(regexp = REGEX) final String role) {
    return roleService.create(role);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<RoleDto> list() {
    return roleService.findAll();
  }

  @GetMapping("{role}")
  @ResponseStatus(HttpStatus.OK)
  public RoleDto retrieve(@PathVariable("role") @Pattern(regexp = REGEX) final String role) {
    return roleService.findOne(role);
  }

  @DeleteMapping("{role}")
  @ResponseStatus(HttpStatus.GONE)
  public String destroy(@PathVariable("role") @Pattern(regexp = REGEX) final String role) {
    return roleService.delete(role);
  }

  @PostMapping("{role}/compose")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public RoleDto compose(
      @PathVariable("role") @Pattern(regexp = REGEX) final String role,
      @RequestBody @NotEmpty(message = "At least one permission is required.")
          Set<String> permissions) {
    return roleService.compose(role, permissions);
  }

  @PostMapping("{role}/decompose")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public RoleDto decompose(
      @PathVariable("role") @Pattern(regexp = REGEX) final String role,
      @RequestBody @NotEmpty(message = "At least one permission is required.")
          Set<String> permissions) {
    return roleService.decompose(role, permissions);
  }
}
