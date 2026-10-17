package com.sparta.msa.lesson.domain.product.repository;

import static com.sparta.msa.lesson.domain.category.entity.QCategory.category;
import static com.sparta.msa.lesson.domain.product.entity.QProduct.product;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.msa.lesson.domain.product.dto.*;
import com.sparta.msa.lesson.domain.product.entity.Product;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductQueryRepository {
  private final JPAQueryFactory queryFactory;

  public List<Product> findProducts(String name, BigDecimal minPrice, BigDecimal maxPrice) {
    return queryFactory
        .selectFrom(product)
        .where(nameContains(name), priceGoe(minPrice), priceLoe(maxPrice))
        .orderBy(product.id.asc())
        .fetch();
  }

  public List<Product> findProductsByParentCategory(Long id) {
    return queryFactory
        .selectFrom(product)
        .join(product.category, category)
        .where(category.parent.id.eq(id))
        .orderBy(product.id.asc())
        .fetch();
  }

  public Page<Product> findPagedProducts(String name, Pageable pageable) {
    var content =
        queryFactory
            .selectFrom(product)
            .where(nameContains(name))
            .orderBy(product.id.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();
    Long total =
        queryFactory.select(product.count()).from(product).where(nameContains(name)).fetchOne();
    return new PageImpl<>(content, pageable, total == null ? 0 : total);
  }

  public List<ProductProjectionDto> findProductDTOs(BigDecimal minPrice) {
    return queryFactory
        .select(new QProductProjectionDto(product.name, product.price, product.stock))
        .from(product)
        .where(priceGoe(minPrice))
        .fetch();
  }

  public List<CategoryProductDto> findCategoryProducts(String name) {
    return queryFactory
        .select(new QCategoryProductDto(category.name, product.name, product.price, product.stock))
        .from(product)
        .join(product.category, category)
        .where(name == null ? null : category.name.eq(name))
        .fetch();
  }

  public List<CategoryProductCountDto> findCategoryProductCounts() {
    return queryFactory
        .select(new QCategoryProductCountDto(category.name, product.id.count()))
        .from(product)
        .join(product.category, category)
        .groupBy(category.id, category.name)
        .orderBy(product.id.count().desc())
        .fetch();
  }

  private BooleanExpression nameContains(String name) {
    return name == null || name.isBlank() ? null : product.name.containsIgnoreCase(name);
  }

  private BooleanExpression priceGoe(BigDecimal price) {
    return price == null ? null : product.price.goe(price);
  }

  private BooleanExpression priceLoe(BigDecimal price) {
    return price == null ? null : product.price.loe(price);
  }
}
