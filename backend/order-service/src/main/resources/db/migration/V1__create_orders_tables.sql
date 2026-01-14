-- V1: Create orders tables

-- Orders table
CREATE TABLE orders (
    id BINARY(16) NOT NULL PRIMARY KEY,
    order_number VARCHAR(50) NOT NULL UNIQUE,
    user_id BINARY(16) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    -- Pricing
    subtotal DECIMAL(12, 2) NOT NULL,
    shipping_cost DECIMAL(10, 2) DEFAULT 0.00,
    tax_amount DECIMAL(10, 2) DEFAULT 0.00,
    discount_amount DECIMAL(10, 2) DEFAULT 0.00,
    total_amount DECIMAL(12, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',

    -- Shipping address
    shipping_street VARCHAR(255),
    shipping_city VARCHAR(100),
    shipping_state VARCHAR(100),
    shipping_zip VARCHAR(20),
    shipping_country VARCHAR(100),

    -- Billing address
    billing_street VARCHAR(255),
    billing_city VARCHAR(100),
    billing_state VARCHAR(100),
    billing_zip VARCHAR(20),
    billing_country VARCHAR(100),

    -- References
    payment_id BINARY(16),

    -- Notes and cancellation
    notes VARCHAR(500),
    cancelled_at TIMESTAMP NULL,
    cancellation_reason VARCHAR(500),

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_orders_order_number (order_number),
    INDEX idx_orders_user_id (user_id),
    INDEX idx_orders_status (status),
    INDEX idx_orders_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Order items table
CREATE TABLE order_items (
    id BINARY(16) NOT NULL PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    product_sku VARCHAR(100) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(10, 2) NOT NULL,
    line_total DECIMAL(12, 2) NOT NULL,

    -- Audit fields
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_order_items_order_id (order_id),
    INDEX idx_order_items_product_id (product_id),
    CONSTRAINT fk_order_items_order FOREIGN KEY (order_id) REFERENCES orders(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
