package com.sparta.msa.lesson.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class DomainException extends RuntimeException {

  private final HttpStatus httpStatus;
  private final String code;

  public DomainException(DomainExceptionCode exceptionCode) {
    super(exceptionCode.getMessage());

    this.httpStatus = exceptionCode.getStatus();
    this.code = exceptionCode.name();
  }
}
