package com.sparta.msa.lesson.domain.cart.mapper;

import com.sparta.msa.lesson.domain.cart.dto.CartItemResponse;
import com.sparta.msa.lesson.domain.cart.entity.CarItem;
import com.sparta.msa.lesson.domain.product.entity.Product;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

// DB Entity를 API 응답에 필요한 값으로 변환합니다. 회원·상품 조회와 Entity 생성은 Service가 담당합니다.
@Component
public class CartItemMapper {

  public CartItemResponse toResponse(CarItem item) {
    // 항목에 상품명·가격을 복사해 저장하지 않고, 연결된 Product의 현재 값을 사용합니다.
    // 현재 EntityGraph를 제거한 상태이므로 아래에서 상품 정보를 읽으면 추가 SELECT가 발생할 수 있습니다.
    Product product = item.getProduct();
    // 한 항목의 금액 = 현재 단가 × 수량. BigDecimal은 소수 금액 계산에 사용합니다.
    BigDecimal subtotal = product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

    return new CartItemResponse(
        item.getId(),
        product.getId(),
        product.getName(),
        product.getPrice(),
        item.getQuantity(),
        subtotal);
  }
}
