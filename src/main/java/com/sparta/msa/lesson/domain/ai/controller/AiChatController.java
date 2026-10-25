package com.sparta.msa.lesson.domain.ai.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.ai.dto.*;
import com.sparta.msa.lesson.domain.ai.service.ChatService;
import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/ai")
public class AiChatController {
  private final ChatService service;

  @PostMapping(value = "/chat", consumes = "text/plain")
  public ApiResponse<ChatResponse> chat(
      @RequestBody @NotBlank @Size(max = 8000) String message,
      @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(service.chat(user.getUserId(), message, false));
  }

  @GetMapping("/marketing")
  public ApiResponse<String> marketing(
      @RequestParam @NotBlank String productName,
      @RequestParam @NotBlank String features,
      @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(
        service.generate(
            user.getUserId(),
            "당신은 상품 마케팅 전문가입니다.",
            "상품: " + productName + ". 특징: " + features + ". 과장하지 않고 광고 문구를 작성해주세요.",
            0.7,
            500));
  }

  @GetMapping("/translate")
  public ApiResponse<String> translate(
      @RequestParam @NotBlank String text,
      @RequestParam(defaultValue = "영어") String targetLanguage,
      @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(
        service.generate(
            user.getUserId(),
            "당신은 전문 번역가입니다.",
            "다음 내용을 " + targetLanguage + "로 번역해주세요: " + text,
            0.1,
            1000));
  }

  @GetMapping("/story")
  public ApiResponse<String> story(
      @RequestParam @NotBlank String topic, @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(
        service.generate(
            user.getUserId(), "당신은 이야기 작가입니다.", "다음 주제로 창의적인 이야기를 작성해주세요: " + topic, 0.9, 500));
  }

  @GetMapping("/summary")
  public ApiResponse<String> summary(
      @RequestParam @NotBlank String text, @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(service.generate(user.getUserId(), "핵심 위주로 정확하게 요약하세요.", text, 0.1, 200));
  }

  @GetMapping("/analysis")
  public ApiResponse<ProductAnalysisResponse> analyze(
      @RequestParam @NotBlank String review, @AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(service.analyze(user.getUserId(), review));
  }
}
