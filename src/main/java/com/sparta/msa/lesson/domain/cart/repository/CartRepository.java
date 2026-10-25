package com.sparta.msa.lesson.domain.cart.repository;

import com.sparta.msa.lesson.domain.cart.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

  // UserId는 Cart.user.id를 의미합니다. 해당 회원의 장바구니가 없을 수도 있어 Optional을 사용합니다.
  Optional<Cart> findByUserId(Long userId);
}
