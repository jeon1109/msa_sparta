package com.sparta.msa.lesson.domain.ai.dto;

public record ProductAnalysisResponse(String sentiment, int score, String summary) {}
