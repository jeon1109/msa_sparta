package com.sparta.msa.lesson.domain.ai.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.ai.dto.*;
import com.sparta.msa.lesson.domain.ai.service.ChatService;
import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai-chat")
public class ChatController {
  private final ChatService service;

  @PostMapping
  public ApiResponse<ChatResponse> chat(
      @Valid @RequestBody ChatRequest request, @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(service.chat(user.getUserId(), request.message(), false));
  }

  @PostMapping("/cached")
  public ApiResponse<ChatResponse> cached(
      @Valid @RequestBody ChatRequest request, @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(service.cached(user.getUserId(), request.message()));
  }

  @PostMapping("/context")
  public ApiResponse<ChatResponse> context(
      @Valid @RequestBody ChatRequest request, @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(service.chat(user.getUserId(), request.message(), true));
  }
}
