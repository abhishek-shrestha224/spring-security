package com.example.bookstore.repository;

import com.example.bookstore.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
  Optional<User> findByUsername(String username);

  boolean existsByUsername(String username);

  void deleteByUsername(String username);
}
