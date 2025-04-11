package com.example.bookstore.repository;

import com.example.bookstore.entity.Permission;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Integer> {

  void deleteByName(final String name);

  Optional<Permission> findByName(final String name);

  boolean existsByName(final String name);

  @Query("from Permission p where p.name like '%'||:name")
  List<Permission> findByResource(@Param("name") final String name);
}
