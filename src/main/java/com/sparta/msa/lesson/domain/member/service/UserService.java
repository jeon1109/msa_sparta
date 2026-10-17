package com.sparta.msa.lesson.domain.member.service;

import com.sparta.msa.lesson.common.exception.*;
import com.sparta.msa.lesson.domain.member.dto.*;
import com.sparta.msa.lesson.domain.member.entity.User;
import com.sparta.msa.lesson.domain.member.mapper.UserMapper;
import com.sparta.msa.lesson.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserResponse createUser(UserRequest request) {
    if (userRepository.findByEmail(request.getEmail()).isPresent())
      throw new DomainException(DomainExceptionCode.DUPLICATE_EMAIL);
    return userMapper.toResponse(
        userRepository.save(
            userMapper.toEntity(request, passwordEncoder.encode(request.getPassword()))));
  }

  public UserResponse getUserById(Long id) {
    User user =
        userRepository
            .findById(id)
            .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_USER));
    return userMapper.toResponse(user);
  }
}
