package com.sparta.msa.lesson.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRequest {

  @NotBlank(message = "사용자 이름은 필수입니다.")
  @Size(min = 2, max = 50, message = "이름은 2자 이상 50자 이하로 입력해주세요.")
  @JsonAlias("name")
  String username;

  @NotBlank(message = "이메일은 필수입니다.")
  @Email(message = "유효한 이메일 형식이 아닙니다.")
  String email;

  @NotBlank(message = "비밀번호는 필수입니다.")
  @Size(min = 8, max = 72, message = "비밀번호는 8~72자로 입력해주세요.")
  String password;

  @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
  @JsonAlias("phone")
  private String phoneNumber;

  private String gender;
}
