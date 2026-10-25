package com.sparta.msa.lesson.domain.product.dto;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ProductDto {

  private final String name;
  private final BigDecimal price;
}
