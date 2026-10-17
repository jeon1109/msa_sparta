package com.sparta.msa.lesson.domain.orders.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record OrderRequest(
    @NotNull @Positive Long userId, @NotEmpty List<@Valid OrderProduct> products) {
  public record OrderProduct(@NotNull @Positive Long id, @NotNull @Min(1) Integer quantity) {}
}
