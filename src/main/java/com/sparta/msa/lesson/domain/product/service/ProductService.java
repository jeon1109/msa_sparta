package com.sparta.msa.lesson.domain.product.service;

import com.sparta.msa.lesson.common.exception.DomainException;
import com.sparta.msa.lesson.common.exception.DomainExceptionCode;
import com.sparta.msa.lesson.domain.category.repository.CategoryRepository;
import com.sparta.msa.lesson.domain.orders.repository.OrderProductRepository;
import com.sparta.msa.lesson.domain.product.dto.ProductRequest;
import com.sparta.msa.lesson.domain.product.dto.ProductResponse;
import com.sparta.msa.lesson.domain.product.entity.Product;
import com.sparta.msa.lesson.domain.product.mapper.ProductMapper;
import com.sparta.msa.lesson.domain.product.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// final 필드인 productRepository를 주입받는 생성자를 자동 생성
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

  private final ProductRepository productRepository;
  private final ProductMapper productMapper;
  private final CategoryRepository categoryRepository;
  private final OrderProductRepository orderProductRepository;

  // 전체 조회
  public List<ProductResponse> getAllProducts() {

    return productRepository.findAll().stream().map(productMapper::toResponse).toList();
  }

  // 단건 조회
  public ProductResponse getProductById(Long id) {

    Product product = getProduct(id);

    return productMapper.toResponse(product);
  }

  // 등록
  @Transactional
  public ProductResponse create(ProductRequest request) {

    validateCategory(request.getCategoryId());
    Product product = productMapper.toEntity(request);

    Product savedProduct = productRepository.save(product);

    return productMapper.toResponse(savedProduct);
  }

  // 수정
  @Transactional
  public ProductResponse update(Long id, ProductRequest request) {

    Product product = getProduct(id);

    validateCategory(request.getCategoryId());
    product.update(
        request.getCategoryId(),
        request.getName(),
        request.getDescription(),
        request.getPrice(),
        request.getStock());

    return productMapper.toResponse(product);
  }

  // 삭제
  @Transactional
  public void deleteById(Long id) {

    Product product = getProduct(id);

    if (orderProductRepository.existsByProductId(id)) {
      throw new DomainException(DomainExceptionCode.PRODUCT_IN_USE);
    }
    productRepository.delete(product);
  }

  // 재고 감소
  @Transactional
  public ProductResponse decreaseStock(Long id, int quantity) {

    Product product = getProduct(id);

    if (quantity <= 0) {
      throw new DomainException(DomainExceptionCode.INVALID_QUANTITY);
    }

    if (product.getStock() < quantity) {
      throw new DomainException(DomainExceptionCode.OUT_OF_STOCK);
    }

    product.decreaseStock(quantity);

    return productMapper.toResponse(product);
  }

  private void validateCategory(Long id) {
    if (id != null && !categoryRepository.existsById(id))
      throw new DomainException(DomainExceptionCode.NOT_FOUND_CATEGORY);
  }

  private Product getProduct(Long id) {

    return productRepository
        .findById(id)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT));
  }
}
