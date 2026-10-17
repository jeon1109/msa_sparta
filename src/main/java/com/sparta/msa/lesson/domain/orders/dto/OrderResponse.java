package com.sparta.msa.lesson.domain.orders.dto;

import com.sparta.msa.lesson.domain.orders.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
    Long id,
    BigDecimal totalPrice,
    OrderStatus status,
    LocalDateTime createdAt,
    OrderUserResponse user,
    List<OrderProductResponse> products) {
  public record OrderUserResponse(Long id, String name) {}

  public record OrderProductResponse(Long id, String name, Integer quantity, BigDecimal price) {}
}
