package com.sparta.msa.lesson.domain.auth.security;

import com.sparta.msa.lesson.domain.member.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetails loadUserByUsername(String email) {
    return new CustomUserDetails(
        userRepository
            .findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다.")));
  }
}
