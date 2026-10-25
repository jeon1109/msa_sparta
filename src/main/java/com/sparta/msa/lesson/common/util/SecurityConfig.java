package com.sparta.msa.lesson.common.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.msa.lesson.common.exception.DomainExceptionCode;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.*;
import org.springframework.security.web.authentication.session.*;
import org.springframework.security.web.context.*;
import org.springframework.security.web.session.ConcurrentSessionFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
  private final ObjectMapper objectMapper;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
      throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public SecurityContextRepository securityContextRepository() {
    return new HttpSessionSecurityContextRepository();
  }

  @Bean
  public SessionAuthenticationStrategy sessionAuthenticationStrategy(SessionRegistry registry) {
    var concurrent = new ConcurrentSessionControlAuthenticationStrategy(registry);
    concurrent.setMaximumSessions(1);
    concurrent.setExceptionIfMaximumExceeded(false);
    return new CompositeSessionAuthenticationStrategy(
        List.of(
            concurrent,
            new ChangeSessionIdAuthenticationStrategy(),
            new RegisterSessionAuthenticationStrategy(registry)));
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http, SessionRegistry registry)
      throws Exception {
    http.csrf(AbstractHttpConfigurer::disable)
        .securityContext(c -> c.securityContextRepository(securityContextRepository()))
        .authorizeHttpRequests(
            a ->
                a.requestMatchers(
                        "/swagger-ui/**",
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/actuator/health",
                        "/error",
                        "/api/auth/register",
                        "/api/auth/registration",
                        "/api/auth/login")
                    .permitAll()
                    .requestMatchers("/api/auth/admin/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/api/**")
                    .hasAnyRole("USER", "ADMIN")
                    .anyRequest()
                    .authenticated())
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .addFilter(
            new ConcurrentSessionFilter(
                registry, e -> writeError(e.getResponse(), DomainExceptionCode.SESSION_EXPIRED)))
        .exceptionHandling(
            e ->
                e.authenticationEntryPoint(
                        (q, r, x) -> writeError(r, DomainExceptionCode.UNAUTHORIZED_ACCESS))
                    .accessDeniedHandler(
                        (q, r, x) -> writeError(r, DomainExceptionCode.ACCESS_DENIED)));
    return http.build();
  }

  private void writeError(HttpServletResponse response, DomainExceptionCode code)
      throws IOException {
    response.setStatus(code.getStatus().value());
    response.setContentType("application/json;charset=UTF-8");
    objectMapper.writeValue(
        response.getWriter(),
        ApiResponse.fail(code.getStatus(), code.name(), code.getMessage()).getBody());
  }
}
