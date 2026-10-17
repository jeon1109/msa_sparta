package com.sparta.msa.lesson.domain.ai.dto;

import jakarta.validation.constraints.*;

public record ChatRequest(@NotBlank @Size(max = 8000) String message) {}
