package com.sparta.msa.lesson.domain.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserDto {

  private String username;

  @Setter // DTO는 계층 간 전달되므로 Setter가 필요한 경우가 많음
  private String email;
}
