package com.sparta.msa.lesson.domain.member.mapper;

import com.sparta.msa.lesson.domain.member.dto.UserDto;
import com.sparta.msa.lesson.domain.member.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// MapStruct가 생성할 구현체를 스프링 Bean으로 등록
@Mapper(componentModel = "spring")
public class UserMapper {

  // 필드명이 다를 경우 @Mapping으로 직접 지정
  @Mapping(source = "email", target = "emailAddress")
  UserDto toDto(Users entity);

  // 필드명이 같다면 별도 매핑 없이 자동으로 변환
  Users toEntity(UserDto dto);
}
