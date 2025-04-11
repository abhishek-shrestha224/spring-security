package com.example.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Permissions")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Permission implements GrantedAuthority {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(nullable = false, updatable = false, unique = true)
  private String name;

  @ManyToMany(mappedBy = "permissions")
  @JsonBackReference
  @ToString.Exclude
  private Set<Role> roles;

  public Permission(String name) {
    id = null;
    this.name = name;
    roles = new HashSet<>();
  }

  @Override
  public String getAuthority() {
    return name;
  }

  public void addToRole(Role role) {
    roles.add(role);
  }

  public void removeFromRole(Role role) {
    roles.remove(role);
  }
}
