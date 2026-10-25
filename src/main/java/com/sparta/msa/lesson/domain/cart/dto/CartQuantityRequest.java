package com.sparta.msa.lesson.domain.cart.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 수량 변경 요청입니다. 대상 항목은 URL의 itemId로 알 수 있으므로 quantity만 받습니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartQuantityRequest {

  // 추가 수량이 아니라 최종 수량입니다. 예: 현재 5개에 3을 보내면 3개가 됩니다.
  @NotNull
  @Positive
  private Integer quantity;
}
