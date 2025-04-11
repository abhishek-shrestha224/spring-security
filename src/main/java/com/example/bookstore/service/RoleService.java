package com.example.bookstore.service;

import com.example.bookstore.dto.res.RoleDto;
import com.example.bookstore.entity.Permission;
import com.example.bookstore.entity.Role;
import com.example.bookstore.exceptions.EmptyCollectionException;
import com.example.bookstore.exceptions.EntityNotFoundException;
import com.example.bookstore.repository.RoleRepository;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoleService {
  private final RoleRepository roleRepository;
  private final PermissionService permissionService;

  public RoleDto create(final String role) {
    final var res = roleRepository.save(new Role(role));
    return new RoleDto(
        res.getName(),
        res.getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
  }

  public RoleDto findOne(final String role) {
    final var res = findRoleEntity(role);
    return new RoleDto(
        res.getName(),
        res.getPermissions().stream().map(Permission::getAuthority).collect(Collectors.toSet()));
  }

  public List<RoleDto> findAll() {
    final var roles = roleRepository.findAll();
    if (roles.isEmpty()) throw new EmptyCollectionException("No roles in db.");

    return roles.stream()
        .map(
            r ->
                new RoleDto(
                    r.getName(),
                    r.getPermissions().stream()
                        .map(Permission::getAuthority)
                        .collect(Collectors.toSet())))
        .toList();
  }

  @Transactional
  public String delete(final String role) {
    if (!roleRepository.existsByName(role))
      throw new EntityNotFoundException("Unable to locate item to delete.");
    roleRepository.deleteByName(role);
    return "SKDOOSH!!!";
  }

  @Transactional
  public RoleDto compose(final String role, final Set<String> permissions) {
    final Role roleEntity = findRoleEntity(role);
    Set<Permission> permissionEntities = new HashSet<>();

    permissions.forEach(
        p -> {
          final Permission permission = permissionService.findPermissionEntity(p);
          permissionEntities.add(permission);
          permission.getRoles().add(roleEntity);
        });

    roleEntity.addPermissions(permissionEntities);
    return new RoleDto(
        roleEntity.getName(),
        roleEntity.getPermissions().stream()
            .map(Permission::getAuthority)
            .collect(Collectors.toSet()));
  }

  @Transactional
  public RoleDto decompose(final String role, final Set<String> permissions) {
    final Role roleEntity = findRoleEntity(role);
    Set<Permission> permissionEntities = new HashSet<>();

    permissions.forEach(
        p -> {
          final Permission permission = permissionService.findPermissionEntity(p);
          permissionEntities.add(permission);
          permission.removeFromRole(roleEntity);
        });

    roleEntity.removePermissions(permissionEntities);
    return new RoleDto(
        roleEntity.getName(),
        roleEntity.getPermissions().stream()
            .map(Permission::getAuthority)
            .collect(Collectors.toSet()));
  }

  //  Utils
  Role getDefaultRole() {
    return roleRepository
        .findByName("REGULAR")
        .orElseThrow(() -> new RuntimeException("Something Went Wrong."));
  }

  Role findRoleEntity(final String role) {
    return roleRepository
        .findByName(role)
        .orElseThrow(() -> new EntityNotFoundException("Role not found."));
  }
}
