package com.example.bookstore.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "Users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  private String firstName;

  private String lastName;

  private Integer age;

  private String username;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String password;

  @OneToOne(mappedBy = "user")
  @JsonBackReference
  private Session session;

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role", referencedColumnName = "id"))
  @JsonManagedReference
  private Set<Role> roles;

  // !  Methods
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    final var permissionStream =
        roles.stream()
            .flatMap(
                role ->
                    role.getPermissions().stream()
                        .map(p -> new SimpleGrantedAuthority(p.getAuthority())));

    return Stream.concat(
            permissionStream,
            roles.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName())))
        .collect(Collectors.toSet());
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  public void addRoles(Set<Role> roles) {
    this.roles.addAll(roles);
  }

  public void removeRoles(Set<Role> roles) {
    this.roles.removeAll(roles);
  }
}
