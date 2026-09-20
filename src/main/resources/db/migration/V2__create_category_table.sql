CREATE TABLE categories (
                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                parent_id BIGINT DEFAULT NULL -- FK: 부모 카테고리의 id를 참조
);