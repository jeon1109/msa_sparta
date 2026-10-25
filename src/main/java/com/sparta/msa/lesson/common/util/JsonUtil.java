package com.sparta.msa.lesson.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa.lesson.common.exception.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JsonUtil {
  private final ObjectMapper objectMapper;

  public String toJson(Object value) {
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new DomainException(DomainExceptionCode.JSON_PROCESSING_ERROR);
    }
  }

  public <T> List<T> fromJsonList(String value, Class<T> type) {
    try {
      return objectMapper.readValue(
          value, objectMapper.getTypeFactory().constructCollectionType(List.class, type));
    } catch (JsonProcessingException e) {
      throw new DomainException(DomainExceptionCode.JSON_PROCESSING_ERROR);
    }
  }
}
