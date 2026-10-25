package com.sparta.msa.lesson.domain.cart.service;

import com.sparta.msa.lesson.common.exception.DomainException;
import com.sparta.msa.lesson.common.exception.DomainExceptionCode;
import com.sparta.msa.lesson.domain.cart.dto.CartItemReqeuset;
import com.sparta.msa.lesson.domain.cart.dto.CartItemResponse;
import com.sparta.msa.lesson.domain.cart.dto.CartResponse;
import com.sparta.msa.lesson.domain.cart.entity.CarItem;
import com.sparta.msa.lesson.domain.cart.entity.Cart;
import com.sparta.msa.lesson.domain.cart.mapper.CartItemMapper;
import com.sparta.msa.lesson.domain.cart.mapper.CartMapper;
import com.sparta.msa.lesson.domain.cart.repository.CartItemRepository;
import com.sparta.msa.lesson.domain.cart.repository.CartRepository;
import com.sparta.msa.lesson.domain.member.entity.User;
import com.sparta.msa.lesson.domain.member.repository.UserRepository;
import com.sparta.msa.lesson.domain.product.entity.Product;
import com.sparta.msa.lesson.domain.product.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
// 조회는 읽기 전용으로 처리하고, 저장·변경·삭제 메서드에는 쓰기 트랜잭션을 따로 적용합니다.
// 현재는 기본 CRUD 학습 단계이므로 락을 사용하지 않습니다. 동시 요청의 수량 유실은 방지하지 않습니다.
@Transactional(readOnly = true)
public class CartService {

  private final CartRepository carts;
  private final CartItemRepository items;
  private final ProductRepository products;
  private final CartMapper cartMapper;
  private final CartItemMapper itemMapper;
  private final UserRepository userRepository;

  // 아직 장바구니가 없으면 DB에 새로 만들지 않고 빈 목록과 합계 0을 반환합니다.
  public CartResponse getCart(Long userId) {
    return carts.findByUserId(userId)
        .map(this::response)
        .orElseGet(() -> new CartResponse(null, userId, List.of(), BigDecimal.ZERO));
  }

  @Transactional
  public CartResponse add(Long userId, CartItemReqeuset request) {
    // 요청에 담긴 ID로 객체만 만드는 것이 아니라, 실제 존재하는 회원·상품을 조회합니다.
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_USER));
    Product product = products.findById(request.getProductId())
        .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_PRODUCT));
    
    // 첫 상품을 담는 시점에만 회원의 장바구니를 생성합니다.
    Cart cart = carts.findByUserId(userId)
        .orElseGet(() -> carts.save(new Cart(user)));
    // 같은 상품의 행을 새로 만들지 않도록 기존 항목이 있는지 확인합니다.
    Optional<CarItem> existing = items.findByCartIdAndProductId(cart.getId(), product.getId());

    // 기존 수량 2 + 추가 요청 3 = 5. 처음 담는 상품이면 항목을 새로 저장합니다.
    // 장바구니에 담는 것만으로 상품 재고를 차감하거나 예약하지는 않습니다.
    if (existing.isPresent()) {
      existing.get().addQuantity(request.getQuantity());
    } else {
      items.save(new CarItem(cart, product, request.getQuantity()));
    }

    // 변경 SQL을 DB에 반영한 다음 목록을 다시 조회합니다. flush는 commit과 다릅니다.
    // 이후 오류가 발생하면 이 트랜잭션의 변경도 롤백될 수 있습니다.
    items.flush();
    return response(cart);
  }

  @Transactional
  public CartResponse changeQuantity(Long userId, Long itemId, int quantity) {
    // 로그인한 회원의 항목인지 확인한 뒤, 합산하지 않고 요청 수량으로 교체합니다.
    CarItem item = ownedItem(userId, itemId);
    // 조회한 Entity는 JPA가 관리하므로 값이 바뀌면 Dirty Checking으로 UPDATE가 반영됩니다.
    item.changeQuantity(quantity);

    items.flush();
    return response(item.getCart());
  }

  @Transactional
  public void remove(Long userId, Long itemId) {
    items.delete(ownedItem(userId, itemId));
  }

  @Transactional
  public void clear(Long userId) {
    carts.findByUserId(userId)
        .ifPresent(cart -> items.deleteAll(items.findByCartIdOrderByIdAsc(cart.getId())));
  }

  // ID만 조회하면 다른 회원의 항목도 변경할 수 있으므로 회원 ID를 함께 조건으로 사용합니다.
  // 없는 항목과 다른 회원의 항목은 모두 404로 처리합니다.
  private CarItem ownedItem(Long userId, Long itemId) {
    return items.findByIdAndCartUserId(itemId, userId)
        .orElseThrow(() -> new DomainException(DomainExceptionCode.NOT_FOUND_CART_ITEM));
  }

  private CartResponse response(Cart cart) {
    // 각 항목을 응답 DTO로 바꾼 뒤, CartMapper에서 장바구니 전체 합계를 계산합니다.
    List<CartItemResponse> itemResponses = items.findByCartIdOrderByIdAsc(cart.getId()).stream()
        .map(itemMapper::toResponse)
        .toList();

    return cartMapper.toResponse(cart, itemResponses);
  }
}
