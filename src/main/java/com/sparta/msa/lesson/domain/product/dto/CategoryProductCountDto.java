package com.sparta.msa.lesson.domain.product.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class CategoryProductCountDto {
  private final String categoryName;
  private final Long count;

  @QueryProjection
  public CategoryProductCountDto(String categoryName, Long count) {
    this.categoryName = categoryName;
    this.count = count;
  }
}
