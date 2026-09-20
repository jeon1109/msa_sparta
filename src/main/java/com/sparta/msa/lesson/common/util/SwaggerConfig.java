package com.sparta.msa.lesson.common.util;

import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@OpenAPIDefinition(
	info = @Info(title = "My API Docs", description = "API 명세서", version = "v1")
)
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI() {
		String jwtSchemeName = "jwtAuth";
		// jwtSchemeName이라는 이름으로 정의된 JWT 보안 방식을 사용
		SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
		// Swagger/OpenAPI에게 "jwtSchemeName이라는 인증 방식을 등록할 건데,
		// 이 인증은 HTTP Bearer 방식이고 토큰 형식은 JWT야"
		Components components = new Components()
			.addSecuritySchemes(jwtSchemeName, new SecurityScheme()
				.name(jwtSchemeName)
				.type(SecurityScheme.Type.HTTP) // HTTP 방식
				.scheme("bearer") // bearer 토큰 방식
				.bearerFormat("JWT")); // 토큰 형식

		return new OpenAPI()
			.addSecurityItem(securityRequirement)
			.components(components);
	}
}
