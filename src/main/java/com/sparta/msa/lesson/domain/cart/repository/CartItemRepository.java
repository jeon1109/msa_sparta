package com.sparta.msa.lesson.domain.cart.repository;

import com.sparta.msa.lesson.domain.cart.entity.CarItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CarItem, Long> {

  // 특정 장바구니의 항목을 ID 오름차순으로 조회합니다.
  // N+1 확인 실습을 위해 @EntityGraph는 제거한 상태입니다. 상품은 LAZY로 조회됩니다.
  List<CarItem> findByCartIdOrderByIdAsc(Long cartId);

  // 같은 상품이 이미 담겼는지 확인합니다. Optional.empty()이면 새 항목을 만듭니다.
  Optional<CarItem> findByCartIdAndProductId(Long cartId, Long productId);

  // Spring Data JPA가 CartUserId를 cart.user.id로 해석해 항목 소유자까지 검사합니다.
  Optional<CarItem> findByIdAndCartUserId(Long id, Long userId);
}
