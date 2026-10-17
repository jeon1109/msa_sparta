package com.sparta.msa.lesson.domain.category.repository;

import com.sparta.msa.lesson.domain.category.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {}
