package com.sparta.msa.lesson.domain.orders.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import com.sparta.msa.lesson.domain.orders.dto.*;
import com.sparta.msa.lesson.domain.orders.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
  private final OrderService orderService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<OrderResponse> create(
      @Valid @RequestBody OrderRequest request,
      @AuthenticationPrincipal CustomUserDetails principal) {
    return ApiResponse.ok(orderService.order(request, principal));
  }

  @GetMapping("/{id}")
  public ApiResponse<OrderResponse> get(
      @PathVariable Long id, @AuthenticationPrincipal CustomUserDetails principal) {
    return ApiResponse.ok(orderService.get(id, principal));
  }
}
