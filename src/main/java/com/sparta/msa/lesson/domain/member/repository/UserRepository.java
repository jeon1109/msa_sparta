package com.sparta.msa.lesson.domain.member.repository;

import com.sparta.msa.lesson.domain.member.entity.Users;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {

  @Query("SELECT u FROM Users u JOIN FETCH u.order")
  List<Users> findAllWithOrders();
  
}
