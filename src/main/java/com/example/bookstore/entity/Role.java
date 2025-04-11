package com.example.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "Roles")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String name;

  @ManyToMany(mappedBy = "roles")
  @JsonBackReference
  @ToString.Exclude
  private Set<User> users;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_permissions",
      joinColumns = @JoinColumn(name = "role", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "permission", referencedColumnName = "id"))
  @JsonManagedReference
  private Set<Permission> permissions;

  public Role(String name) {
    id = null;
    this.name = name;
    users = new HashSet<>();
    permissions = new HashSet<>();
  }

  public void assignToUser(User user) {
    users.add(user);
  }   public void unassignFromUser(User user) {
    users.remove(user);
  }

  public void addPermissions(Set<Permission> newPermissions) {
    permissions.addAll(newPermissions);
  }

  public void removePermissions(Set<Permission> permissionToRemove) {
    permissions.removeAll(permissionToRemove);
  }
}
