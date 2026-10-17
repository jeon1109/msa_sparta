package com.sparta.msa.lesson.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class CategoryProductDto {
  private final String categoryName;
  private final String productName;
  private final BigDecimal price;
  private final Integer stock;

  @QueryProjection
  public CategoryProductDto(
      String categoryName, String productName, BigDecimal price, Integer stock) {
    this.categoryName = categoryName;
    this.productName = productName;
    this.price = price;
    this.stock = stock;
  }
}
