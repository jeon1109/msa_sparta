package com.sparta.msa.lesson.domain.member.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.member.dto.*;
import com.sparta.msa.lesson.domain.member.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
  public ApiResponse<UserResponse> get(@PathVariable Long id) {
    return ApiResponse.ok(userService.getUserById(id));
  }
}
