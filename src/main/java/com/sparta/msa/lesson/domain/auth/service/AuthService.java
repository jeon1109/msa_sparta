package com.sparta.msa.lesson.domain.auth.service;

import com.sparta.msa.lesson.common.exception.*;
import com.sparta.msa.lesson.domain.auth.dto.*;
import com.sparta.msa.lesson.domain.auth.security.*;
import com.sparta.msa.lesson.domain.member.dto.*;
import com.sparta.msa.lesson.domain.member.entity.*;
import com.sparta.msa.lesson.domain.member.repository.UserRepository;
import com.sparta.msa.lesson.domain.member.service.UserService;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.context.*;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
  private final UserService userService;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;
  private final SessionAuthenticationStrategy sessionAuthenticationStrategy;
  private final SecurityContextRepository securityContextRepository;
  private final UserSessionManager userSessionManager;

  public UserResponse register(UserRequest request) {
    return userService.createUser(request);
  }

  @Transactional
  public UserResponse registerAdmin(UserRequest request) {
    UserResponse result = userService.createUser(request);
    User user = userRepository.findById(result.getId()).orElseThrow();
    user.changeRole(Role.ADMIN);
    return result;
  }

  public LoginResponse login(
      LoginRequest login, HttpServletRequest request, HttpServletResponse response) {
    Authentication authentication;
    try {
      authentication =
          authenticationManager.authenticate(
              UsernamePasswordAuthenticationToken.unauthenticated(login.getEmail(), login.getPassword()));
    } catch (AuthenticationException ex) {
      throw new DomainException(DomainExceptionCode.INVALID_CREDENTIALS);
    }
    sessionAuthenticationStrategy.onAuthentication(authentication, request, response);
    var context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
    securityContextRepository.saveContext(context, request, response);
    return LoginResponse.from((CustomUserDetails) authentication.getPrincipal());
  }

  @Transactional(readOnly = true)
  public void forceLogout(Long userId) {
    userSessionManager.logoutAll(
        userRepository
            .findById(userId)
            .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_USER))
            .getEmail());
  }
}
