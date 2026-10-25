package com.sparta.msa.lesson.domain.cart.entity;

import com.sparta.msa.lesson.domain.member.entity.User;
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

// 회원의 장바구니를 나타냅니다. user_id UNIQUE 제약으로 회원당 하나만 저장할 수 있습니다.
@Entity
@Table(name = "carts", uniqueConstraints = @UniqueConstraint(columnNames = "user_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  // 객체에서는 User로 연결하고, DB에서는 user_id 외래 키로 저장합니다.
  // LAZY는 연결된 회원의 실제 정보가 필요할 때 조회하는 설정입니다.
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  public Cart(User user) {
    this.user = user;
  }
}
