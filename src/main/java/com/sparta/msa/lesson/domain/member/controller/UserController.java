package com.sparta.msa.lesson.domain.member.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.member.dto.UserRequest;
import com.sparta.msa.lesson.domain.member.dto.UserResponse;
import com.sparta.msa.lesson.domain.member.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

  private final UserService userService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<UserResponse> create(@Valid @RequestBody UserRequest request) {
    return ApiResponse.ok(userService.createUser(request));
  }

  @GetMapping("/{id}")
  public ApiResponse<UserResponse> get(@PathVariable Long id, @PathVariable String email) {
    return ApiResponse.ok(userService.getUserById(id, email));
  }

  @GetMapping("/{id}/me")
  public ApiResponse<UserResponse> getMyInfo(@PathVariable Long id, @PathVariable String email) {
    return ApiResponse.ok(userService.getUserById(id, email));
  }

}
