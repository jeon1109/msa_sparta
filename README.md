# 🚀 스파르타 MSA 과정 교안 예시 코드

> 스파르타 MSA 과정 **Part 01** 강의에서 사용하는 Spring Boot 기반 예시 프로젝트입니다.
> 주차·일차별 브랜치로 나뉘어 있어, 강의 진도에 맞춰 코드를 따라가며 학습할 수 있습니다.

![Java](https://img.shields.io/badge/Java-21-007396?logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.11-6DB33F?logo=springboot&logoColor=white)
![Spring Cloud](https://img.shields.io/badge/Spring%20Cloud-2025.0.1-6DB33F?logo=spring&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-8.14.4-02303A?logo=gradle&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?logo=postgresql&logoColor=white)
![Redis](https://img.shields.io/badge/Redis-DC382D?logo=redis&logoColor=white)

---

## 📚 목차

- [브랜치 구성](#-브랜치-구성)
- [기술 스택](#-기술-스택)
- [프로젝트 구조](#-프로젝트-구조)
- [시작하기](#-시작하기)

---

## 🌿 브랜치 구성

각 주차는 `original`(시작 코드)과 `day-XX`(일차별 완성 코드) 브랜치로 구성되며, `extra`는 추가 학습 브랜치입니다.

| 주차 | 시작 코드 | Day 01 | Day 02 | Day 03 | Day 04 / 추가 학습 |
|:---:|:---:|:---:|:---:|:---:|:---:|
| **Week 01** | `week-01/original` | `week-01/day-01` | `week-01/day-02` | `week-01/day-03` | `week-01/day-04` |
| **Week 02** | `week-02/original` | `week-02/day-01` | `week-02/day-02` | `week-02/day-03` | `week-02/extra` (추가 학습) |

```bash
# 원하는 강의 단계의 브랜치로 이동
git checkout week-01/day-01
```

---

## 🛠 기술 스택

| 분류 | 사용 기술 |
|---|---|
| **Language / Build** | Java 21, Gradle 8.14 |
| **Framework** | Spring Boot 3.5, Spring Cloud 2025.0 |
| **Web Server** | Undertow (Tomcat 대체) |
| **Persistence** | Spring Data JPA, QueryDSL 5.0, Flyway |
| **Database / Cache** | PostgreSQL, Redis (Spring Session) |
| **Communication** | Spring Cloud OpenFeign, Spring Retry |
| **Security** | Spring Security, JJWT 0.11 |
| **Validation** | Spring Validation (Hibernate Validator) |
| **Mapping** | MapStruct 1.5, Lombok |
| **API Docs** | springdoc-openapi (Swagger UI) |
| **Monitoring** | Spring Boot Actuator |
| **Test** | JUnit 5, Spring Boot Test |

---

## 📁 프로젝트 구조

```
sparta-msa-lesson-part-01
├── build.gradle
├── settings.gradle
├── gradle/wrapper
└── src
    ├── main
    │   ├── java/com/sparta/msa/lesson
    │   │   └── LessonApplication.java
    │   └── resources
    │       └── application.yml
    └── test
        └── java/com/sparta/msa/lesson
            └── LessonApplicationTests.java
```

---

## ▶️ 시작하기

### 1. 사전 준비

- **JDK 21**
- **PostgreSQL**
- **Redis**

### 2. 클론 및 빌드

```bash
git clone <repository-url>
cd sparta-msa-lesson-part-01

./gradlew build
```

### DB 연결 및 IntelliJ 디버그 설정

PostgreSQL을 실행하고 `sparta` 데이터베이스가 준비되어 있는지 확인합니다.
IntelliJ의 **Run > Edit Configurations > LessonApplication > Environment variables**에 다음 변수를 설정한 뒤 Debug를 실행합니다.

- `DB_PASSWORD`: Docker PostgreSQL의 계정 비밀번호 (로컬 기본값 `postgres`)
- `DB_USERNAME`: DB 계정 (기본값 `postgres`)
- `DB_URL`: JDBC 접속 주소 (기본값 `jdbc:postgresql://127.0.0.1:5433/sparta`)

현재 로컬 DB는 Docker의 `postgres-dev` 컨테이너이며, Windows PostgreSQL과의 5432 포트 충돌을 피하려고 호스트의 5433 포트에 연결했습니다. 기존 데이터 볼륨은 그대로 사용합니다. `postgres-dev-port5432-backup`은 복구용으로 중지 상태를 유지해야 하며, 두 컨테이너를 동시에 시작하면 안 됩니다. 실제 운영 비밀번호는 환경 변수로 지정합니다. PowerShell에서 실행하는 경우에는 현재 터미널에서 `$env:DB_PASSWORD`를 설정한 뒤 `./gradlew.bat bootRun`을 실행합니다.

빌드 성공은 DB 연결 성공을 의미하지 않습니다. 현재 테스트는 DB 접속을 검사하지 않습니다.
`password authentication failed`는 계정/비밀번호 오류, `database "sparta" does not exist`는 DB 미생성, `Connection refused`는 DB 서비스 또는 포트 문제입니다.
현재 `ddl-auto: none`이므로 애플리케이션이 테이블을 자동 생성하지 않습니다. 접속 성공 후 `users` 테이블이 없으면 별도로 스키마를 준비해야 합니다.

Docker Redis는 `redis-dev`의 호스트 포트 6380을 사용합니다. `REDIS_HOST`, `REDIS_PORT`, `REDIS_PASSWORD`로 재정의할 수 있습니다. `redis-dev-port6379-backup`은 복구용이며 중지 상태를 유지합니다.

IntelliJ에서 이전 Spring Cloud 버전으로 실행되면 실행 구성 목록의 **LessonApplication (Gradle)**을 선택하고 Debug를 실행합니다. 이 구성은 `bootRun`으로 현재 `build.gradle`의 의존성을 사용합니다. 기본 Spring Boot 실행 구성을 사용하려면 Gradle 프로젝트를 다시 동기화해야 합니다.

### 3. 실행

```bash
./gradlew bootRun
```

### 4. 테스트

```bash
./gradlew test
```

---

<div align="center">

**Happy Coding! 🎉**

</div>


## 2026-10-17 강의 코드 통합

회원가입·Redis 세션 인증·카테고리·상품 검색·주문·Spring AI 코드를 반영했습니다. [현재 API 및 Swagger 가이드](docs/API_SPEC_AND_SWAGGER_GUIDE.md)를 확인하세요. AI는 기본 비활성입니다. 예전 메모리 admin/1234 로그인 대신 회원가입 후 JSON 로그인으로 인증합니다.
