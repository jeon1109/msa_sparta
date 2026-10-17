package com.sparta.msa.lesson.domain.auth.security;

import com.sparta.msa.lesson.domain.member.entity.*;
import java.io.Serializable;
import java.util.*;
import lombok.Getter;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class CustomUserDetails implements UserDetails, CredentialsContainer, Serializable {
  private static final long serialVersionUID = 1L;
  private final Long userId;
  private final String email;
  private final Role role;
  private String password;

  public CustomUserDetails(User user) {
    userId = user.getId();
    email = user.getEmail();
    role = user.getRole();
    password = user.getPassword();
  }

  public String getUsername() {
    return email;
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
  }

  public void eraseCredentials() {
    password = null;
  }
}
