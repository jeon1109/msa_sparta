package com.sparta.msa.lesson.domain.cart.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import com.sparta.msa.lesson.domain.cart.dto.CartItemReqeuset;
import com.sparta.msa.lesson.domain.cart.dto.CartQuantityRequest;
import com.sparta.msa.lesson.domain.cart.dto.CartResponse;
import com.sparta.msa.lesson.domain.cart.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 요청 흐름: Controller → CartService → Repository / Entity → Mapper → 응답 DTO
// 회원 ID는 요청 본문이 아니라 Spring Security의 로그인 정보에서 가져옵니다.
@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

  private final CartService cartService;

  // GET /api/cart: 로그인한 회원의 장바구니를 조회합니다.
  @GetMapping
  public ApiResponse<CartResponse> get(@AuthenticationPrincipal CustomUserDetails user) {
    return ApiResponse.ok(cartService.getCart(user.getUserId()));
  }

  // POST /api/cart/items: 같은 상품이면 수량을 더합니다. @Valid는 DTO의 입력 조건을 검사합니다.
  @PostMapping("/items")
  public ApiResponse<CartResponse> add(
      @AuthenticationPrincipal CustomUserDetails user,
      @Valid @RequestBody CartItemReqeuset request) {
    return ApiResponse.ok(cartService.add(user.getUserId(), request));
  }

  // PATCH /api/cart/items/{itemId}: quantity는 추가 수량이 아니라 변경할 최종 수량입니다.
  @PatchMapping("/items/{itemId}")
  public ApiResponse<CartResponse> change(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable Long itemId,
      @Valid @RequestBody CartQuantityRequest request) {
    return ApiResponse.ok(
        cartService.changeQuantity(user.getUserId(), itemId, request.getQuantity()));
  }

  // itemId는 상품 ID(productId)가 아니라 장바구니 항목의 ID입니다.
  @DeleteMapping("/items/{itemId}")
  public ApiResponse<Void> remove(
      @AuthenticationPrincipal CustomUserDetails user,
      @PathVariable Long itemId) {
    cartService.remove(user.getUserId(), itemId);
    return ApiResponse.ok();
  }

  // 장바구니 자체는 남겨두고, 그 안에 담긴 항목만 모두 삭제합니다.
  @DeleteMapping("/items")
  public ApiResponse<Void> clear(@AuthenticationPrincipal CustomUserDetails user) {
    cartService.clear(user.getUserId());
    return ApiResponse.ok();
  }
}
