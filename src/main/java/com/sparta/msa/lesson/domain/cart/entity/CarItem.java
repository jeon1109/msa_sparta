package com.sparta.msa.lesson.domain.cart.entity;

import com.sparta.msa.lesson.domain.product.entity.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 장바구니에 담긴 상품 한 종류입니다. 같은 장바구니·상품 조합의 중복 행은 UNIQUE로 막습니다.
@Entity
@Table(
    name = "cart_items",
    uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "product_id"}))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CarItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  // 이 항목이 어느 장바구니에 속하는지 연결합니다.
  @JoinColumn(name = "cart_id", nullable = false)
  private Cart cart;

  @ManyToOne(fetch = FetchType.LAZY)
  // 상품 ID를 외래 키로 저장하고, 상품명·가격은 연결된 Product에서 조회합니다.
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(nullable = false)
  private Integer quantity;

  public CarItem(Cart cart, Product product, int quantity) {
    this.cart = cart;
    this.product = product;
    changeQuantity(quantity);
  }

  // PATCH에서 사용합니다. 기존 수량을 새 값으로 교체하며, 0개는 삭제 API로 처리합니다.
  public void changeQuantity(int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("수량은 1개 이상이어야 합니다.");
    }

    this.quantity = quantity;
  }

  // POST에서 같은 상품을 다시 담을 때 사용합니다.
  public void addQuantity(int quantity) {
    // 더한 결과가 Integer의 최대값을 넘지 않는지도 덧셈 전에 확인합니다.
    if (quantity <= 0 || quantity > Integer.MAX_VALUE - this.quantity) {
      throw new IllegalArgumentException("합산 수량이 허용 범위를 벗어났습니다.");
    }

    changeQuantity(this.quantity + quantity);
  }
}
