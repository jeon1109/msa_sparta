CREATE TABLE carts (
 id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 user_id BIGINT NOT NULL UNIQUE REFERENCES users(id)
);
CREATE TABLE cart_items (
 id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
 cart_id BIGINT NOT NULL REFERENCES carts(id) ON DELETE CASCADE,
 product_id BIGINT NOT NULL REFERENCES products(id),
 quantity INTEGER NOT NULL CHECK (quantity > 0),
 version BIGINT NOT NULL DEFAULT 0,
 CONSTRAINT uk_cart_items_cart_product UNIQUE (cart_id,product_id)
);
CREATE INDEX idx_cart_items_product ON cart_items(product_id);
