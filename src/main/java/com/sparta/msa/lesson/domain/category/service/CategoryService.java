package com.sparta.msa.lesson.domain.category.service;

import com.sparta.msa.lesson.common.exception.*;
import com.sparta.msa.lesson.common.util.JsonUtil;
import com.sparta.msa.lesson.domain.category.dto.*;
import com.sparta.msa.lesson.domain.category.entity.Category;
import com.sparta.msa.lesson.domain.category.repository.CategoryRepository;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;
import org.springframework.transaction.support.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {
  private static final String CACHE_KEY = "lesson:categories";
  private final CategoryRepository categoryRepository;
  private final StringRedisTemplate redis;
  private final JsonUtil jsonUtil;

  public List<CategoryResponse> findAll() {
    List<Category> all = categoryRepository.findAll();
    Map<Long, List<Category>> children =
        all.stream()
            .filter(c -> c.getParent() != null)
            .collect(Collectors.groupingBy(c -> c.getParent().getId()));
    return all.stream()
        .filter(c -> c.getParent() == null)
        .map(c -> toResponse(c, children))
        .toList();
  }

  private CategoryResponse toResponse(Category c, Map<Long, List<Category>> children) {
    return new CategoryResponse(
        c.getId(),
        c.getName(),
        children.getOrDefault(c.getId(), List.of()).stream()
            .map(child -> toResponse(child, children))
            .toList());
  }

  public List<CategoryResponse> findAllForCacheAside() {
    String cached = redis.opsForValue().get(CACHE_KEY);
    if (cached != null) return jsonUtil.fromJsonList(cached, CategoryResponse.class);
    List<CategoryResponse> result = findAll();
    redis.opsForValue().set(CACHE_KEY, jsonUtil.toJson(result), Duration.ofHours(1));
    return result;
  }

  @Transactional
  public CategoryResponse create(CategoryRequest request) {
    Category parent =
        request.parentCategoryId() == null ? null : getCategory(request.parentCategoryId());
    Category saved =
        categoryRepository.save(Category.builder().name(request.name()).parent(parent).build());
    afterCommit(() -> redis.delete(CACHE_KEY));
    return new CategoryResponse(saved.getId(), saved.getName(), List.of());
  }

  @Transactional
  public CategoryResponse saveWriteThrough(CategoryRequest request) {
    CategoryResponse result = create(request);
    // Publish only after the database transaction has committed.
    afterCommit(
        () -> redis.opsForValue().set(CACHE_KEY, jsonUtil.toJson(findAll()), Duration.ofHours(1)));
    return result;
  }

  public Category getCategory(Long id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_CATEGORY));
  }

  private void afterCommit(Runnable action) {
    TransactionSynchronizationManager.registerSynchronization(
        new TransactionSynchronization() {
          public void afterCommit() {
            action.run();
          }
        });
  }
}
