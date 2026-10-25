package com.sparta.msa.lesson.domain.cart.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 한 항목의 응답입니다. Entity 전체를 노출하지 않고 화면에 필요한 값만 전달합니다.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

  // 수정·삭제 API의 URL에 사용하는 항목 ID입니다. productId와 구분합니다.
  private Long itemId;

  private Long productId;

  private String productName;

  // 조회 시점의 상품 단가입니다.
  private BigDecimal unitPrice;

  private Integer quantity;

  // 이 항목의 소계 = unitPrice × quantity입니다.
  private BigDecimal subtotal;
}
