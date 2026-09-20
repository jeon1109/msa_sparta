CREATE TABLE orders
(
    id          BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    user_id     BIGINT         NOT NULL,
    total_price DECIMAL(10, 2) NOT NULL,
    status      VARCHAR(20)    NOT NULL,
    created_at  TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 연결 테이블: `order_products`
CREATE TABLE order_products
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    order_id   BIGINT         NOT NULL, -- FK: 어떤 주문에 속하는지
    product_id BIGINT         NOT NULL, -- FK: 어떤 상품인지
    quantity   INT            NOT NULL, -- 주문 수량
    price      DECIMAL(10, 2) NOT NULL  -- 주문 시점의 상품 가격
);