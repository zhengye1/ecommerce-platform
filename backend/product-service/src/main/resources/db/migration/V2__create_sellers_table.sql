-- V2: Create sellers table for marketplace functionality

CREATE TABLE sellers (
    id BINARY(16) NOT NULL PRIMARY KEY,
    user_id BINARY(16) NOT NULL UNIQUE,
    shop_name VARCHAR(100) NOT NULL,
    shop_description TEXT,
    logo_url VARCHAR(500),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    commission_rate DECIMAL(5, 4) DEFAULT 0.1000,
    business_license VARCHAR(100),
    contact_email VARCHAR(255),
    contact_phone VARCHAR(20),
    verified_at TIMESTAMP NULL,
    rating DECIMAL(3, 2) DEFAULT 0.00,
    review_count INT DEFAULT 0,
    total_sales INT DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_sellers_user_id (user_id),
    INDEX idx_sellers_shop_name (shop_name),
    INDEX idx_sellers_status (status),
    INDEX idx_sellers_rating (rating)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
