package com.sparta.msa.lesson.domain.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductAnalysisResponse {

  private String sentiment;

  private int score;

  private String summary;
}
