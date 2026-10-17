package com.sparta.msa.lesson.domain.category.dto;

import jakarta.validation.constraints.*;

public record CategoryRequest(
    @NotBlank @Size(max = 255) String name, @Positive Long parentCategoryId) {}
