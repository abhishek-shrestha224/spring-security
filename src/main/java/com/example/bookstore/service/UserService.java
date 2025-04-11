package com.example.bookstore.service;

import com.example.bookstore.dto.req.SignupRequest;
import com.example.bookstore.dto.res.UserDto;
import com.example.bookstore.entity.Role;
import com.example.bookstore.entity.User;
import com.example.bookstore.exceptions.EntityNotFoundException;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.utils.UserMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
  private final UserRepository userRepository;
  private final RoleService roleService;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  public UserDto create(SignupRequest request) {
    final var role = roleService.getDefaultRole();
    final var user = userMapper.userEntity(request);
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    user.setRoles(Set.of(role));
    return userMapper.userDto(userRepository.save(user));
  }

  public List<UserDto> findAll() {
    return userRepository.findAll().stream().map(userMapper::userDto).toList();
  }

  public UserDto findOne(final String username) {
    final var user = findUserEntity(username);
    return userMapper.userDto(user);
  }

  @Transactional
  public String delete(final String username) {
    if (!userRepository.existsByUsername(username))
      throw new EntityNotFoundException("User not found.");
    userRepository.deleteByUsername(username);
    return "SKDOOSH!!";
  }

  @Transactional
  public UserDto compose(final String username, final Set<String> roles) {
    log.info("username:{} roles{}", username, roles.toString());
    final var user = findUserEntity(username);
    log.info("user entity: {}", user);
    final Set<Role> roleEntities = new HashSet<>();

    roles.forEach(
        r -> {
          log.info("role string: {}", r);
          final var role = roleService.findRoleEntity(r);
          roleEntities.add(role);
          log.info("updated roles set: {}", roleEntities.toString());
          role.assignToUser(user);
          log.info("updated role: {}", role);
        });
    user.addRoles(roleEntities);
    log.info("final user {}", user);
    return userMapper.userDto(user);
  }

  @Transactional
  public UserDto decompose(final String username, final Set<String> roles) {
    final var user = findUserEntity(username);
    final Set<Role> roleEntities = new HashSet<>();

    roles.forEach(
        r -> {
          final var role = roleService.findRoleEntity(r);
          roleEntities.add(role);
          role.unassignFromUser(user);
        });
    user.removeRoles(roleEntities);
    return userMapper.userDto(user);
  }

  //  Utils
  User findUserEntity(final String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new EntityNotFoundException("User not found."));
  }
}
