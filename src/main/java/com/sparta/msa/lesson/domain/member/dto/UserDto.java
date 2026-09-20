package com.sparta.msa.lesson.domain.member.dto;

import lombok.Setter;

public class UserDto {

  private String username;

  @Setter // DTO는 계층 간 전달되므로 Setter가 필요한 경우가 많음
  private String email;
  // ... 서비스 로직에 필요한 다른 필드 ...
}
