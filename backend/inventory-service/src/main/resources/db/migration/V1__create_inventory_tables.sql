-- V1: Create inventory tables

CREATE TABLE inventory_items (
    id BINARY(16) NOT NULL PRIMARY KEY,
    product_id BINARY(16) NOT NULL UNIQUE,
    sku VARCHAR(100) NOT NULL UNIQUE,
    quantity_available INT NOT NULL DEFAULT 0,
    quantity_reserved INT NOT NULL DEFAULT 0,
    reorder_level INT DEFAULT 10,
    reorder_quantity INT DEFAULT 50,
    warehouse_location VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_inventory_product_id (product_id),
    INDEX idx_inventory_sku (sku)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE stock_reservations (
    id BINARY(16) NOT NULL PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    quantity INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    expires_at TIMESTAMP NULL,
    confirmed_at TIMESTAMP NULL,
    released_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_reservations_order_id (order_id),
    INDEX idx_reservations_product_id (product_id),
    INDEX idx_reservations_status (status),
    INDEX idx_reservations_expires (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
