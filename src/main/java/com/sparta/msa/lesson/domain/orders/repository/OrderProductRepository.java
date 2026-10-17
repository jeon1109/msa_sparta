package com.sparta.msa.lesson.domain.orders.repository;

import com.sparta.msa.lesson.domain.orders.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
  boolean existsByProductId(Long productId);
}
