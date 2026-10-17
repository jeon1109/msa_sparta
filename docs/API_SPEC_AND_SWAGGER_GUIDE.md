# 강의 반영 API 및 Swagger 확인 가이드

2026-10-17 · 작업 브랜치 `work/jong-gil`

이 문서는 현재 소스 기준입니다. API 실행 테스트는 수행하지 않았습니다. 이전 가이드의 `admin/1234` 메모리 로그인과 폼 로그인은 제거되었으며, DB 회원과 Redis 세션으로 인증합니다.

## 실행

JDK 21과 Docker PostgreSQL/Redis를 준비하고 IntelliJ Gradle 프로젝트를 새로고침합니다.
`LessonApplication (Gradle)`으로 실행합니다. 기본 접속은 DB `127.0.0.1:5433/sparta`, Redis `127.0.0.1:6380`입니다.
기존 복구용 backup 컨테이너는 시작하지 않습니다.

- Swagger: http://localhost:8080/swagger-ui/index.html
- OpenAPI: http://localhost:8080/v3/api-docs
- DB 스키마: 기존 V1을 유지하고 V2로 회원 phone/gender/role 및 상품 version 필드를 추가합니다. 소스만 추가했으며 DB에 직접 실행하지 않았습니다. 앱 시작 시 Flyway가 적용합니다.
- 이미 적용한 V1 파일을 수정하거나 Flyway 체크섬 오류를 무시하지 마세요. 기존 DB의 마이그레이션 이력에 따라 별도 확인이 필요할 수 있습니다.

## Swagger 순서

### 1. 회원가입: POST /api/auth/register

```json
{"username":"홍길동","email":"student@example.com","password":"Password123!","phoneNumber":"010-1234-5678"}
```

`name`과 `phone`은 각각 username과 phoneNumber의 별칭으로도 사용할 수 있습니다. 가입 결과 `data.id`를 기록하세요. 비밀번호는 BCrypt로 저장되고 응답에 포함되지 않습니다. 이메일 중복은 409입니다.

### 2. 로그인: POST /api/auth/login

```json
{"email":"student@example.com","password":"Password123!"}
```

같은 브라우저의 SESSION 쿠키로 이후 요청을 인증합니다. JWT Authorize는 사용하지 않습니다.
`GET /api/auth/status`로 로그인 상태를 확인할 수 있습니다.
로그아웃은 `POST /api/auth/logout`입니다. 같은 계정으로 새 로그인하면 이전 세션은 만료됩니다. 시크릿 창으로 두 번째 로그인 후 기존 창에서 API를 호출해 확인할 수 있습니다.

강의의 JSON 세션 로그인 흐름에 맞춰 CSRF 검증은 비활성화했습니다. 외부 사이트에 배포할 때는 쿠키 정책과 CSRF 구성을 다시 설정해야 합니다.

### 3. 카테고리

`POST /api/categories`에 `{"name":"전자제품"}`을 보냅니다. 반환한 `data.id`를 parentCategoryId로 전달해 하위 카테고리를 만들 수 있습니다.

```json
{"name":"노트북","parentCategoryId":1}
```

- GET `/api/categories`: 계층 조회
- GET `/api/categories/caches`: 현재 세션에 JSON 문자열 캐시. 다른 세션에서 변경한 내용은 세션 캐시에 바로 반영되지 않는 강의용 예제입니다.
- GET `/api/categories/cache-aside`: Redis 캐시 조회, 미스 시 DB 조회 및 1시간 TTL 저장
- POST `/api/categories/write-through`: 저장 후 커밋이 완료되면 공용 캐시 갱신
- POST `/api/categories/write-around`: DB 저장 후 공용 캐시 무효화
- GET `/api/categories/product-counts`: 카테고리별 상품 수. 원문의 `/order-counts`도 같은 상품 통계의 별칭입니다.

공용 Redis 캐시는 `lesson:categories` 키를 사용합니다. DB와 Redis를 하나의 원자적 트랜잭션으로 취급하지 않습니다.

### 4. 상품

`POST /api/products`:

```json
{"categoryId":1,"name":"강의 노트북","description":"Swagger 확인용","price":120000,"stock":10}
```

categoryId는 실제 생성한 ID로 변경하거나 생략하세요. 반환한 `data.id`를 사용합니다. 응답에는 id, categoryId, name, description, price, stock, createdAt이 포함됩니다.

- GET `/api/products`: 전체 목록
- GET `/api/products/{id}`: 단건
- PUT `/api/products/{id}`: 전체 수정. 등록과 동일한 JSON
- DELETE `/api/products/{id}`: 삭제, 204. 주문에 사용된 상품은 409 PRODUCT_IN_USE
- PATCH `/api/products/{id}/stock?quantity=2`: 재고 감소
- GET `/api/products/search?name=노트북&minPrice=1000&maxPrice=200000`: QueryDSL 동적 검색
- GET `/api/products/paged?page=0&size=20`: 이름 필터 선택, ID 오름차순 페이지 조회
- GET `/api/products/by-parent-category/{id}`: 부모 카테고리의 직계 자식 카테고리 상품
- GET `/api/products/projections?minPrice=1000`: Q DTO 생성자 프로젝션
- GET `/api/products/category-products?categoryName=노트북`: 조인 프로젝션

### 5. 주문

`POST /api/orders`:

```json
{"userId":1,"products":[{"id":1,"quantity":2}]}
```

userId는 로그인한 회원 ID, 상품 id는 생성한 상품 ID로 바꿉니다. 관리자는 다른 회원 주문도 처리할 수 있습니다.
주문 생성, 주문 상품 저장, 재고 감소는 하나의 트랜잭션입니다. 중간 상품의 재고가 부족하면 전체가 롤백됩니다. 상품의 낙관적 잠금 충돌은 409입니다.
`GET /api/orders/{id}`로 조회합니다. 주문 응답에는 상태 CREATED, 총액, 사용자 및 주문 상품이 포함됩니다. 결제·취소 API는 강의 발췌에 없어 추가하지 않았습니다.

### 6. Redis 데이터 타입

POST `/api/redis-demo`는 로그인한 사용자 전용 `lesson:demo:{userId}:*`에 String/카운터/List/Set/Hash/Sorted Set 실습을 실행하고 결과를 반환합니다.
키의 TTL은 1시간입니다. 같은 사용자의 실습 키만 초기화하며, 세션과 카테고리 캐시 키에는 접근하지 않습니다. 자동 실행되는 테스트는 아닙니다.

### 7. Spring AI (선택)

기본적으로 비활성화되어 키 없이 앱을 실행할 수 있습니다. 비활성 상태에서 AI API를 호출하면 503 AI_SERVICE_UNAVAILABLE입니다.
강의의 Gemini OpenAI 호환 API를 사용하려면 다음 환경 변수를 설정한 뒤 앱을 재시작합니다.

```powershell
$env:AI_CHAT_MODEL = 'openai'
$env:AI_API_KEY = '<Google AI Studio에서 발급한 실제 키>'
$env:AI_MODEL = '<사용 가능한 Gemini 모델 ID>'
```

기본 AI_BASE_URL은 `https://generativelanguage.googleapis.com/v1beta/openai`, AI_COMPLETIONS_PATH는 `/chat/completions`입니다. 실제 모델 ID는 사용 중인 제공자에서 확인하세요.
다른 OpenAI 호환 제공자를 사용할 때는 AI_BASE_URL, AI_COMPLETIONS_PATH, AI_MODEL을 해당 제공자의 값으로 지정합니다. 키는 소스에 저장하지 않습니다.

- POST `/api/ai-chat`: `{"message":"안녕하세요"}`
- POST `/api/ai-chat/context`: 쇼핑 도우미 시스템 프롬프트 적용. 대화 이력을 자동 저장하지는 않습니다.
- POST `/api/ai-chat/cached`: 동일 사용자·동일 메시지 응답을 1시간 캐시
- POST `/api/ai/chat`: text/plain 본문
- GET `/api/ai/marketing?productName=옥수수&features=고소함`
- GET `/api/ai/translate?text=안녕하세요&targetLanguage=영어`
- GET `/api/ai/story?topic=우주여행`
- GET `/api/ai/summary?text=요약할본문`
- GET `/api/ai/analysis?review=품질이좋아요`: sentiment, score, summary 구조화 응답

캐시 미스 및 일반 AI 요청은 사용자별 하루 10회(Asia/Seoul)로 제한합니다. AI_DAILY_LIMIT으로 변경합니다. 제공자 호출 실패도 시도 횟수에 포함됩니다. 캐시 히트는 횟수를 소비하지 않습니다. 실제 AI 호출 및 요금 발생 테스트는 실행하지 않았습니다.

### 8. 관리자

강의의 공개 관리자 가입은 권한 상승을 막기 위해 관리자 인증을 요구하도록 통합했습니다.
최초 관리자가 필요하면 DB에서 가입된 개발용 계정 한 개의 role을 직접 ADMIN으로 변경한 후 재로그인합니다. 예시 SQL은 자동 실행하지 않습니다.

```sql
UPDATE users SET role = 'ADMIN' WHERE email = 'student@example.com';
```

- POST `/api/auth/admin/register`: 관리자 계정 추가
- POST `/api/auth/admin/users/{userId}/logout`: 해당 회원의 Redis 세션 전체 삭제

## 응답 및 오류

성공은 `{"data":...}`, 데이터 없는 성공은 `{}`, 삭제는 204입니다. 오류는 `{"error":{"errorCode":"...","errorMessage":"..."}}`입니다.
입력 오류 400, 인증 실패 401, 접근 거부 403, 없는 엔티티 404, 중복 이메일/동시 수정/DB 충돌 409를 반환합니다.

## 원문 보존 및 통합 범위

[전체 발췌](LECTURE_EXCERPTS.txt)를 그대로 보존했습니다. DI/빌더의 전후 비교, 잘못된 JPQL, 브라우저에 키를 노출하는 반례, Redis 설정 CLI 및 단위 테스트 예시는 실행 코드로 중복 등록하지 않았습니다.
원문이 잘린 프로시저 호출, 미구현 사용량 서비스, Write-back 비동기 저장 예제는 문서에 보존했습니다. 데이터 유실 가능성이 있는 미완성 Write-back API는 추가하지 않았습니다. 실제 API는 위 목록에 있는 기능입니다.

Spring AI는 Boot 3.5와 맞는 1.1.0 및 정식 `spring-ai-starter-model-openai`를 사용합니다. 원문의 1.0.0-M6 스타터와 MySQL 설정을 그대로 덮어쓰지 않았습니다.
참고: [Spring AI](https://github.com/spring-projects/spring-ai/tree/1.1.x), [Spring Session](https://docs.spring.io/spring-session/reference/3.5/configuration/redis.html), [Chat Completions API](https://developers.openai.com/api/reference/resources/chat).
