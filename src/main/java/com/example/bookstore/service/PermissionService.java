package com.example.bookstore.service;

import com.example.bookstore.entity.Permission;
import com.example.bookstore.exceptions.EmptyCollectionException;
import com.example.bookstore.exceptions.EntityNotFoundException;
import com.example.bookstore.repository.PermissionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
  private final PermissionRepository permissionRepository;

  public String create(final String permissionName) {
    final var res = permissionRepository.save(new Permission(permissionName));
    return res.getAuthority();
  }

  public List<String> findAll() {
    final var permissions = permissionRepository.findAll();
    if (permissions.isEmpty()) throw new EmptyCollectionException("No permissions in db.");
    return permissions.stream().map(Permission::getAuthority).toList();
  }

  public List<String> findByResource(final String resourceName) {
    final var permissions = permissionRepository.findByResource(resourceName);
    if (permissions.isEmpty()) throw new EmptyCollectionException("No permissions in db.");
    return permissions.stream().map(Permission::getAuthority).toList();
  }

  @Transactional
  public String deleteOne(final String permissionName) {
    if (!permissionRepository.existsByName(permissionName))
      throw new EntityNotFoundException("Permission not found.");
    permissionRepository.deleteByName(permissionName);
    return "SKDOOSH!!!";
  }

  //  Utils
  Permission findPermissionEntity(final String permissionName) {
    return permissionRepository
        .findByName(permissionName)
        .orElseThrow(() -> new EntityNotFoundException("Permission not found"));
  }
}
