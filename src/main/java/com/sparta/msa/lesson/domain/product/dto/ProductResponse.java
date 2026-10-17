package com.sparta.msa.lesson.domain.product.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder // 생성의 편의성을 위해 빌더 패턴 사용
@JsonInclude(JsonInclude.Include.NON_NULL) // null 필드는 JSON에서 제외
public class ProductResponse {

  private final Long id;
  private final Long categoryId;
  private final Integer stock;
  private final String name;
  private final String description;
  private final BigDecimal price;

  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private final LocalDateTime createdAt;
}
