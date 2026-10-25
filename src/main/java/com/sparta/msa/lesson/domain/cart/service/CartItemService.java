package com.sparta.msa.lesson.domain.cart.service;

import com.sparta.msa.lesson.domain.cart.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 초기 초안으로 남아 있는 클래스입니다. 현재 항목 추가·수정·삭제는 CartService가 담당합니다.
@Service
@Transactional(readOnly = true)
public class CartItemService {

  private final CartItemRepository cartItemRepository;

  public CartItemService(CartItemRepository cartItemRepository) {
    this.cartItemRepository = cartItemRepository;
  }

}
