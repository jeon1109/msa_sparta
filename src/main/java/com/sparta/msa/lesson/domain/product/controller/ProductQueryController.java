package com.sparta.msa.lesson.domain.product.controller;

import com.sparta.msa.lesson.common.util.ApiResponse;
import com.sparta.msa.lesson.domain.product.dto.*;
import com.sparta.msa.lesson.domain.product.mapper.ProductMapper;
import com.sparta.msa.lesson.domain.product.repository.ProductQueryRepository;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/products")
public class ProductQueryController {
  private final ProductQueryRepository queries;
  private final ProductMapper mapper;

  @GetMapping("/search")
  public ApiResponse<List<ProductResponse>> search(
      @RequestParam(required = false) String name,
      @RequestParam(required = false) @PositiveOrZero BigDecimal minPrice,
      @RequestParam(required = false) @PositiveOrZero BigDecimal maxPrice) {
    return ApiResponse.ok(
        queries.findProducts(name, minPrice, maxPrice).stream().map(mapper::toResponse).toList());
  }

  @GetMapping("/paged")
  public ApiResponse<Page<ProductResponse>> paged(
      @RequestParam(required = false) String name,
      @RequestParam(defaultValue = "0") @Min(0) int page,
      @RequestParam(defaultValue = "20") @Min(1) @Max(100) int size) {
    return ApiResponse.ok(
        queries.findPagedProducts(name, PageRequest.of(page, size)).map(mapper::toResponse));
  }

  @GetMapping("/by-parent-category/{id}")
  public ApiResponse<List<ProductResponse>> parent(@PathVariable Long id) {
    return ApiResponse.ok(
        queries.findProductsByParentCategory(id).stream().map(mapper::toResponse).toList());
  }

  @GetMapping("/projections")
  public ApiResponse<List<ProductProjectionDto>> projections(
      @RequestParam(required = false) BigDecimal minPrice) {
    return ApiResponse.ok(queries.findProductDTOs(minPrice));
  }

  @GetMapping("/category-products")
  public ApiResponse<List<CategoryProductDto>> categories(
      @RequestParam(required = false) String categoryName) {
    return ApiResponse.ok(queries.findCategoryProducts(categoryName));
  }
}
