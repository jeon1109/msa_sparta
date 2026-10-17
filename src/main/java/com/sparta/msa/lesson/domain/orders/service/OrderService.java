package com.sparta.msa.lesson.domain.orders.service;

import com.sparta.msa.lesson.common.exception.*;
import com.sparta.msa.lesson.domain.auth.security.CustomUserDetails;
import com.sparta.msa.lesson.domain.member.entity.Role;
import com.sparta.msa.lesson.domain.member.repository.UserRepository;
import com.sparta.msa.lesson.domain.orders.dto.*;
import com.sparta.msa.lesson.domain.orders.entity.Order;
import com.sparta.msa.lesson.domain.orders.mapper.OrderMapper;
import com.sparta.msa.lesson.domain.orders.repository.OrderRepository;
import com.sparta.msa.lesson.domain.product.repository.ProductRepository;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
  private final OrderRepository orderRepository;
  private final UserRepository userRepository;
  private final ProductRepository productRepository;
  private final OrderMapper mapper;

  @Transactional
  public OrderResponse order(OrderRequest request, CustomUserDetails principal) {
    authorize(request.userId(), principal);
    var user =
        userRepository
            .findById(request.userId())
            .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_USER));
    var order = Order.builder().user(user).totalPrice(BigDecimal.ZERO).build();
    BigDecimal total = BigDecimal.ZERO;
    for (var item : request.products()) {
      var product =
          productRepository
              .findById(item.id())
              .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT));
      if (product.getStock() < item.quantity())
        throw new DomainException(DomainExceptionCode.OUT_OF_STOCK);
      product.decreaseStock(item.quantity());
      order.addProduct(product, item.quantity());
      total = total.add(product.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
    }
    order.setTotalPrice(total);
    return mapper.toResponse(orderRepository.saveAndFlush(order));
  }

  public OrderResponse get(Long id, CustomUserDetails principal) {
    var order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_ORDER));
    authorize(order.getUser().getId(), principal);
    return mapper.toResponse(order);
  }

  private void authorize(Long userId, CustomUserDetails principal) {
    if (principal.getRole() != Role.ADMIN && !principal.getUserId().equals(userId))
      throw new DomainException(DomainExceptionCode.ACCESS_DENIED);
  }
}
