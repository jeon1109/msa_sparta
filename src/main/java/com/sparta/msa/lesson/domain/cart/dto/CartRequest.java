package com.sparta.msa.lesson.domain.cart.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

// 초기 초안으로 남아 있는 DTO입니다. 현재 장바구니 API에서는 사용하지 않습니다.
// 실제 회원 ID는 요청으로 받지 않고 로그인 정보에서 가져옵니다.
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CartRequest {

  private Long userId;

}
