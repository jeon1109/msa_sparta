package com.sparta.msa.lesson.domain.cart.dto;

import java.math.BigDecimal;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 장바구니 전체 응답입니다. 항목 목록과 각 항목 소계의 합을 반환합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

  // 장바구니가 아직 생성되지 않은 회원은 null입니다.
  private Long id;

  private Long userId;

  private List<CartItemResponse> items;

  private BigDecimal totalPrice;
}
