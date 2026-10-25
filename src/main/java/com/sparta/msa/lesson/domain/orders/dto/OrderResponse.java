package com.sparta.msa.lesson.domain.orders.dto;

import com.sparta.msa.lesson.domain.orders.entity.OrderStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

  private Long id;

  private BigDecimal totalPrice;

  private OrderStatus status;

  private LocalDateTime createdAt;

  private OrderUserResponse user;

  private List<OrderProductResponse> products;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderUserResponse {

    private Long id;

    private String name;
  }

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderProductResponse {

    private Long id;

    private String name;

    private Integer quantity;

    private BigDecimal price;
  }
}
