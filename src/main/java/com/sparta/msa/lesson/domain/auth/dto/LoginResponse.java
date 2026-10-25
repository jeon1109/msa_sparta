package com.sparta.msa.lesson.domain.auth.dto;

import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import com.sparta.msa.lesson.domain.member.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {

  private Long userId;

  private String email;

  private Role role;

  public static LoginResponse from(CustomUserDetails user) {
    return new LoginResponse(user.getUserId(), user.getEmail(), user.getRole());
  }
}
