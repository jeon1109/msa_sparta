package com.sparta.msa.lesson.domain.product.entity;

import com.sparta.msa.lesson.domain.category.entity.Category;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long categoryId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "category_id", insertable = false, updatable = false)
  private Category category;

  @Version private Long version;

  @Column(nullable = false)
  private String name;

  private String description;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private Integer stock;

  @Column(nullable = false)
  private LocalDateTime createdAt;

  @Builder
  public Product(
      Long categoryId, String name, String description, BigDecimal price, Integer stock) {

    if (price.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("가격은 음수일 수 없습니다.");
    }

    if (stock < 0) {
      throw new IllegalArgumentException("재고는 음수일 수 없습니다.");
    }

    this.categoryId = categoryId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
    this.createdAt = LocalDateTime.now();
  }

  public void update(
      Long categoryId, String name, String description, BigDecimal price, Integer stock) {
    this.categoryId = categoryId;
    this.name = name;
    this.description = description;
    this.price = price;
    this.stock = stock;
  }

  public void decreaseStock(int quantity) {

    if (quantity <= 0) {
      throw new IllegalArgumentException("구매 수량은 1개 이상이어야 합니다.");
    }

    if (this.stock < quantity) {
      throw new IllegalArgumentException("재고가 부족합니다.");
    }

    this.stock -= quantity;
  }
}
