package com.sparta.msa.lesson.domain.orders.mapper;

import com.sparta.msa.lesson.domain.orders.dto.OrderResponse;
import com.sparta.msa.lesson.domain.orders.entity.*;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderMapper {
  OrderResponse toResponse(Order order);

  @Mapping(target = "id", source = "product.id")
  @Mapping(target = "name", source = "product.name")
  OrderResponse.OrderProductResponse toProductResponse(OrderProduct item);
}
