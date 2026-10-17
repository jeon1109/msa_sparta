package com.sparta.msa.lesson.domain.orders.repository;

import com.sparta.msa.lesson.domain.orders.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {}
