package com.sparta.msa.lesson.domain.product.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;

@Getter
public class ProductRequest {

  private Long categoryId;

  @NotBlank(message = "상품명은 필수입니다.")
  @Size(max = 255)
  private String name;

  private String description;

  @NotNull(message = "가격은 필수입니다.")
  @Positive(message = "가격은 0보다 커야 합니다.")
  @Digits(integer = 8, fraction = 2)
  private BigDecimal price;

  @NotNull(message = "재고는 필수입니다.")
  @PositiveOrZero(message = "재고는 0 이상이어야 합니다.")
  private Integer stock;
}
