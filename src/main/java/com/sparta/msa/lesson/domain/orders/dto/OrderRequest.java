package com.sparta.msa.lesson.domain.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {

  @NotNull
  @Positive
  private Long userId;

  @NotEmpty
  private List<@Valid OrderProduct> products;

  @Getter
  @Setter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class OrderProduct {

    @NotNull
    @Positive
    private Long id;

    @NotNull
    @Min(1)
    private Integer quantity;
  }
}
