package com.sparta.msa.lesson.domain.product.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.product.dto.ProductRequest;
import com.sparta.msa.lesson.domain.product.dto.ProductResponse;
import com.sparta.msa.lesson.domain.product.service.ProductService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

  private final ProductService productService;

  // 전체 조회
  @GetMapping
  public ApiResponse<List<ProductResponse>> getAllProducts() {

    return ApiResponse.ok(productService.getAllProducts());
  }

  // 단건 조회
  @GetMapping("/{id}")
  public ApiResponse<ProductResponse> getProductById(@PathVariable Long id) {

    return ApiResponse.ok(productService.getProductById(id));
  }

  // 등록
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<ProductResponse> create(@Valid @RequestBody ProductRequest request) {

    return ApiResponse.ok(productService.create(request));
  }

  // 수정
  @PutMapping("/{id}")
  public ApiResponse<ProductResponse> update(
      @PathVariable Long id, @Valid @RequestBody ProductRequest request) {

    return ApiResponse.ok(productService.update(id, request));
  }

  // 삭제
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {

    productService.deleteById(id);
  }

  // 재고 감소
  @PatchMapping("/{id}/stock")
  public ApiResponse<ProductResponse> decreaseStock(
      @PathVariable Long id, @RequestParam int quantity) {

    return ApiResponse.ok(productService.decreaseStock(id, quantity));
  }
}
