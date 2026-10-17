package com.sparta.msa.lesson.domain.member.mapper;

import com.sparta.msa.lesson.domain.member.dto.*;
import com.sparta.msa.lesson.domain.member.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
  @Mapping(target = "username", source = "name")
  UserResponse toResponse(User user);

  @Mapping(target = "name", source = "request.username")
  @Mapping(target = "password", source = "encodedPassword")
  @Mapping(target = "phone", source = "request.phoneNumber")
  @Mapping(target = "role", constant = "USER")
  User toEntity(UserRequest request, String encodedPassword);
}
