-- V3: Add seller_id foreign key to products table

-- Add seller_id column to products table
ALTER TABLE products
    ADD COLUMN seller_id BINARY(16) NOT NULL AFTER category_id;

-- Add index for seller_id
ALTER TABLE products
    ADD INDEX idx_products_seller (seller_id);

-- Add foreign key constraint
ALTER TABLE products
    ADD CONSTRAINT fk_product_seller FOREIGN KEY (seller_id) REFERENCES sellers(id) ON DELETE RESTRICT;
