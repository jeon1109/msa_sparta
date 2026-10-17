package com.sparta.msa.lesson.domain.auth.dto;

import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import com.sparta.msa.lesson.domain.member.entity.Role;

public record LoginResponse(Long userId, String email, Role role) {
  public static LoginResponse from(CustomUserDetails user) {
    return new LoginResponse(user.getUserId(), user.getEmail(), user.getRole());
  }
}
