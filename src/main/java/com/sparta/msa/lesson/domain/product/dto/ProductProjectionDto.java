package com.sparta.msa.lesson.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class ProductProjectionDto {
  private final String name;
  private final BigDecimal price;
  private final Integer stock;

  @QueryProjection
  public ProductProjectionDto(String name, BigDecimal price, Integer stock) {
    this.name = name;
    this.price = price;
    this.stock = stock;
  }
}
