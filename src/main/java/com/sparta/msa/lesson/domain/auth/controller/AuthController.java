package com.sparta.msa.lesson.domain.auth.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.auth.dto.*;
import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import com.sparta.msa.lesson.domain.auth.service.AuthService;
import com.sparta.msa.lesson.domain.member.dto.*;
import jakarta.servlet.http.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {
  private final AuthService authService;

  @PostMapping({"/register", "/registration"})
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<UserResponse> register(@Valid @RequestBody UserRequest request) {
    return ApiResponse.ok(authService.register(request));
  }

  @PostMapping("/admin/register")
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<UserResponse> registerAdmin(@Valid @RequestBody UserRequest request) {
    return ApiResponse.ok(authService.registerAdmin(request));
  }

  @PostMapping("/login")
  public ApiResponse<LoginResponse> login(
      @Valid @RequestBody LoginRequest login,
      HttpServletRequest request,
      HttpServletResponse response) {
    return ApiResponse.ok(authService.login(login, request, response));
  }

  @GetMapping("/status")
  public ApiResponse<LoginResponse> status(@AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(LoginResponse.from(user));
  }

  @PostMapping("/logout")
  public ApiResponse<Void> logout(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    new SecurityContextLogoutHandler().logout(request, response, authentication);
    return ApiResponse.ok();
  }

  @PostMapping("/admin/users/{userId}/logout")
  public ApiResponse<Void> forceLogout(@PathVariable Long userId) {
    authService.forceLogout(userId);
    return ApiResponse.ok();
  }
}
