package com.sparta.msa.lesson.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DomainExceptionCode {
  NOT_FOUND_CART_ITEM(HttpStatus.NOT_FOUND, "장바구니 상품을 찾을 수 없습니다."),
  PRODUCT_IN_USE(HttpStatus.CONFLICT, "주문에 포함된 상품은 삭제할 수 없습니다."),
  NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
  NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "카테고리를 찾을 수 없습니다."),
  NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),
  DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 사용 중인 이메일입니다."),
  OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "상품 재고가 부족합니다."),
  INVALID_QUANTITY(HttpStatus.BAD_REQUEST, "구매 수량은 1개 이상이어야 합니다."),
  UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "인증되지 않은 접근입니다."),
  INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 올바르지 않습니다."),
  ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
  SESSION_EXPIRED(HttpStatus.UNAUTHORIZED, "다른 기기에서 로그인하여 현재 세션이 만료되었습니다."),
  JSON_PROCESSING_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "JSON 데이터 처리 중 오류가 발생했습니다."),
  AI_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "AI 서비스를 현재 사용할 수 없습니다."),
  AI_RESPONSE_ERROR(HttpStatus.BAD_GATEWAY, "AI 응답 처리 중 오류가 발생했습니다."),
  AI_QUOTA_EXCEEDED(HttpStatus.TOO_MANY_REQUESTS, "오늘 AI 호출 횟수를 초과했습니다.");
  private final HttpStatus status;
  private final String message;
}
