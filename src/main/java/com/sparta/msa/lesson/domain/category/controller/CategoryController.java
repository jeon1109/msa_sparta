package com.sparta.msa.lesson.domain.category.controller;

import com.sparta.msa.lesson.common.util.*;
import com.sparta.msa.lesson.domain.category.dto.*;
import com.sparta.msa.lesson.domain.category.service.CategoryService;
import com.sparta.msa.lesson.domain.product.dto.*;
import com.sparta.msa.lesson.domain.product.repository.ProductQueryRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryController {
  private final CategoryService categoryService;
  private final ProductQueryRepository queries;
  private final JsonUtil jsonUtil;

  @GetMapping
  public ApiResponse<List<CategoryResponse>> list() {
    return ApiResponse.ok(categoryService.findAll());
  }

  @PostMapping({"", "/write-around"})
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<CategoryResponse> create(
      @Valid @RequestBody CategoryRequest request, HttpSession session) {
    session.removeAttribute("cachedCategoriesJson");
    return ApiResponse.ok(categoryService.create(request));
  }

  @GetMapping("/caches")
  public ApiResponse<List<CategoryResponse>> sessionCache(HttpSession session) {
    String json = (String) session.getAttribute("cachedCategoriesJson");
    if (json == null) {
      var result = categoryService.findAll();
      session.setAttribute("cachedCategoriesJson", jsonUtil.toJson(result));
      return ApiResponse.ok(result);
    }
    return ApiResponse.ok(jsonUtil.fromJsonList(json, CategoryResponse.class));
  }

  @GetMapping("/cache-aside")
  public ApiResponse<List<CategoryResponse>> cacheAside() {
    return ApiResponse.ok(categoryService.findAllForCacheAside());
  }

  @PostMapping("/write-through")
  @ResponseStatus(HttpStatus.CREATED)
  public ApiResponse<CategoryResponse> writeThrough(
      @Valid @RequestBody CategoryRequest request, HttpSession session) {
    session.removeAttribute("cachedCategoriesJson");
    return ApiResponse.ok(categoryService.saveWriteThrough(request));
  }

  @GetMapping({"/order-counts", "/product-counts"})
  public ApiResponse<List<CategoryProductCountDto>> counts() {
    return ApiResponse.ok(queries.findCategoryProductCounts());
  }
}
