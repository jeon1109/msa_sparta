package com.sparta.msa.lesson.domain.auth.security;

import lombok.RequiredArgsConstructor;
import org.springframework.session.*;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserSessionManager {
  private final FindByIndexNameSessionRepository<? extends Session> sessionRepository;

  public void logoutAll(String email) {
    sessionRepository.findByPrincipalName(email).keySet().forEach(sessionRepository::deleteById);
  }
}
