-- V1: Create notifications table

CREATE TABLE notifications (
    id BINARY(16) NOT NULL PRIMARY KEY,
    user_id BINARY(16) NOT NULL,
    type VARCHAR(50) NOT NULL,
    channel VARCHAR(20) NOT NULL,
    recipient VARCHAR(255) NOT NULL,
    subject VARCHAR(255),
    content TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    failure_reason VARCHAR(500),
    retry_count INT DEFAULT 0,
    reference_id VARCHAR(255),
    reference_type VARCHAR(50),
    sent_at TIMESTAMP NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_notifications_user_id (user_id),
    INDEX idx_notifications_status (status),
    INDEX idx_notifications_type (type),
    INDEX idx_notifications_reference (reference_id, reference_type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
