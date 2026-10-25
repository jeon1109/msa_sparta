package com.sparta.msa.lesson.domain.product.mapper;

import com.sparta.msa.lesson.domain.product.dto.ProductRequest;
import com.sparta.msa.lesson.domain.product.dto.ProductResponse;
import com.sparta.msa.lesson.domain.product.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

  Product toEntity(ProductRequest request);

  ProductResponse toResponse(Product product);
}
