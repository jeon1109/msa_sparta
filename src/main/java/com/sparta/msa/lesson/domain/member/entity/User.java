package com.sparta.msa.lesson.domain.member.entity;

import com.sparta.msa.lesson.domain.orders.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

@Entity // 이 클래스는 JPA가 관리하는 엔티티입니다.
@Getter // Lombok: Getter 메서드를 자동 생성합니다.
@DynamicInsert // 값이 null이 아닌 필드만으로 INSERT 쿼리를 생성합니다.
@DynamicUpdate // 변경된 필드만으로 UPDATE 쿼리를 생성합니다.
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA는 기본 생성자를 필요로 합니다.
@Table(name = "users") // 'users'라는 이름의 테이블과 매핑됩니다.
public class User {

  @Id // 이 필드가 테이블의 Primary Key(기본 키)입니다.
  @GeneratedValue(strategy = GenerationType.IDENTITY) // PK 생성을 DB의 AUTO_INCREMENT에 위임합니다.
  Long id;

  @Column(nullable = false, length = 50) // 'username' 컬럼, NOT NULL, 길이 50
  String name;

  @Column(nullable = false, unique = true) // 'email' 컬럼, NOT NULL, UNIQUE
  String email;

  @Column(nullable = false)
  String password;

  @Column(length = 20)
  String phone;

  @Column(length = 20)
  String gender;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  Role role = Role.USER;

  @CreationTimestamp // 엔티티가 생성될 때의 시간이 자동으로 기록됩니다.
  @Column(nullable = false, updatable = false)
  LocalDateTime createdAt;

  @UpdateTimestamp // 엔티티가 수정될 때의 시간이 자동으로 기록됩니다.
  @Column
  LocalDateTime updatedAt;

  // User.java의 컬렉션 필드에 어노테이션 추가
  @BatchSize(size = 100) // 100개씩 묶어서 조회
  @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
  private List<Order> orders = new ArrayList<>();

  public void changeRole(Role role) {
    this.role = role;
  }

  @Builder // 빌더 패턴으로 객체를 생성할 수 있게 합니다.
  public User(String name, String email, String password, String phone, String gender, Role role) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.phone = phone;
    this.gender = gender;
    this.role = role == null ? Role.USER : role;
  }
}
