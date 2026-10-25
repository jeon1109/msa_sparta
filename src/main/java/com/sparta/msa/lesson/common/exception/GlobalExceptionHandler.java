package com.sparta.msa.lesson.common.exception;

import com.sparta.msa.lesson.common.util.ApiResponse;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.*;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.*;

@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(DomainException.class)
  public ResponseEntity<ApiResponse<Void>> domain(DomainException ex) {
    return ApiResponse.fail(ex.getHttpStatus(), ex.getCode(), ex.getMessage());
  }

  @ExceptionHandler(BindException.class)
  public ResponseEntity<ApiResponse<Void>> validation(BindException ex) {
    return ApiResponse.fail(
        HttpStatus.BAD_REQUEST,
        "VALIDATION_ERROR",
        ex.getBindingResult().getAllErrors().stream()
            .map(DefaultMessageSourceResolvable::getDefaultMessage)
            .collect(Collectors.joining(", ")));
  }

  @ExceptionHandler({
    HttpMessageNotReadableException.class,
    MethodArgumentTypeMismatchException.class,
    MissingServletRequestParameterException.class,
    ConstraintViolationException.class,
    HandlerMethodValidationException.class,
    IllegalArgumentException.class
  })
  public ResponseEntity<ApiResponse<Void>> badRequest(Exception ex) {
    return ApiResponse.fail(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "요청 형식 또는 값이 올바르지 않습니다.");
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<ApiResponse<Void>> denied(Exception ex) {
    return ApiResponse.fail(HttpStatus.FORBIDDEN, "ACCESS_DENIED", "접근 권한이 없습니다.");
  }

  @ExceptionHandler({
    DataIntegrityViolationException.class,
    OptimisticLockingFailureException.class
  })
  public ResponseEntity<ApiResponse<Void>> conflict(Exception ex) {
    return ApiResponse.fail(
        HttpStatus.CONFLICT, "DATA_CONFLICT", "중복 데이터, 연관 데이터 또는 동시 수정으로 처리할 수 없습니다.");
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> other(Exception ex) {
    log.error("Server error", ex);
    return ApiResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER_ERROR", "서버 오류가 발생했습니다.");
  }
}
