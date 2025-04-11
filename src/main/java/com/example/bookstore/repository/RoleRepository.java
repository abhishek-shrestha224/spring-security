package com.example.bookstore.repository;

import com.example.bookstore.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
  Optional<Role> findByName(final String name);

  boolean existsByName(final String name);

  void deleteByName(final String name);
}
