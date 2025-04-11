package com.example.bookstore.controller;

import com.example.bookstore.service.PermissionService;

import java.util.List;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("permissions")
@RequiredArgsConstructor
public class PermissionController {

  private final String REGEX = "^[a-z]*$";
  private final PermissionService permissionService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public String create(
      @RequestParam("action") @Pattern(regexp = REGEX) final String action,
      @RequestParam("resource") @Pattern(regexp = REGEX) final String resource) {
    return permissionService.create(action + ":" + resource);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasRole('ADMIN')")
  public List<String> show() {
    return permissionService.findAll();
  }

  @GetMapping("filter")
  @ResponseStatus(HttpStatus.OK)
  @PreAuthorize("hasAuthority('view:permission')")
  public List<String> retrieve(
      @RequestParam("resource") @Pattern(regexp = REGEX) final String resource) {
    return permissionService.findByResource(resource);
  }

  @DeleteMapping
  @ResponseStatus(HttpStatus.GONE)
  public String destroy(
      @RequestParam("action") @Pattern(regexp = REGEX) final String action,
      @RequestParam("resource") @Pattern(regexp = REGEX) final String resource) {
    return permissionService.deleteOne(action + ":" + resource);
  }
}
