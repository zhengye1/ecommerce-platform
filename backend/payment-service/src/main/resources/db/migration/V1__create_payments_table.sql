-- V1: Create payments table

CREATE TABLE payments (
    id BINARY(16) NOT NULL PRIMARY KEY,
    order_id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    amount DECIMAL(12, 2) NOT NULL,
    currency VARCHAR(3) DEFAULT 'USD',
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    payment_method VARCHAR(30) NOT NULL,
    provider_name VARCHAR(50),
    provider_transaction_id VARCHAR(255),
    failure_reason VARCHAR(500),
    refund_amount DECIMAL(12, 2),
    refunded_at TIMESTAMP NULL,
    completed_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_payments_order_id (order_id),
    INDEX idx_payments_user_id (user_id),
    INDEX idx_payments_status (status),
    UNIQUE INDEX idx_payments_order_unique (order_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
