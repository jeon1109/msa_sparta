package com.sparta.msa.lesson.domain.cart.mapper;

import com.sparta.msa.lesson.domain.cart.dto.CartItemResponse;
import com.sparta.msa.lesson.domain.cart.dto.CartResponse;
import com.sparta.msa.lesson.domain.cart.entity.Cart;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Component;

// 개별 항목 변환은 CartItemMapper가 담당하고, 여기서는 장바구니 전체 응답을 조립합니다.
@Component
public class CartMapper {

  public CartResponse toResponse(Cart cart, List<CartItemResponse> items) {
    // getSubtotal로 각 항목의 소계만 꺼내고, 0부터 차례로 더합니다. 빈 목록이면 합계는 0입니다.
    BigDecimal totalPrice = items.stream()
        .map(CartItemResponse::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    return new CartResponse(cart.getId(), cart.getUser().getId(), items, totalPrice);
  }
}
