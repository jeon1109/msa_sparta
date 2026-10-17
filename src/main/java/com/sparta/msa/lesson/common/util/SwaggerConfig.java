package com.sparta.msa.lesson.common.util;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.*;

@Configuration
@OpenAPIDefinition(
    info =
        @Info(
            title = "Sparta MSA Lesson API",
            version = "v1",
            description = "회원가입 → 로그인 → 카테고리/상품 → 주문. 로그인 후 브라우저 SESSION 쿠키로 인증합니다."))
public class SwaggerConfig {
  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI();
  }
}
