package com.sparta.msa.lesson.domain.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 상품 추가 요청입니다. cartId와 userId는 받지 않고 서버가 로그인 정보로 결정합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemReqeuset {

  @NotNull
  @Positive
  private Long productId;

  // 추가할 수량입니다. @NotNull은 누락을, @Positive는 0 이하를 거절합니다.
  @NotNull
  @Positive
  private Integer quantity;
}
