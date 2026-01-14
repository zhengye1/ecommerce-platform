-- V1: Create products and categories tables

-- Categories table
CREATE TABLE categories (
    id BINARY(16) NOT NULL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    image_url VARCHAR(500),
    parent_id BINARY(16),
    display_order INT DEFAULT 0,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_categories_slug (slug),
    INDEX idx_categories_parent (parent_id),
    CONSTRAINT fk_category_parent FOREIGN KEY (parent_id) REFERENCES categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Products table
CREATE TABLE products (
    id BINARY(16) NOT NULL PRIMARY KEY,
    sku VARCHAR(100) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10, 2) NOT NULL,
    compare_at_price DECIMAL(10, 2),
    cost_price DECIMAL(10, 2),
    category_id BINARY(16),
    brand VARCHAR(100),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    stock_quantity INT NOT NULL DEFAULT 0,
    low_stock_threshold INT DEFAULT 10,
    featured BOOLEAN NOT NULL DEFAULT FALSE,
    weight_kg DECIMAL(10, 3),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NULL ON UPDATE CURRENT_TIMESTAMP,
    created_by VARCHAR(255),
    updated_by VARCHAR(255),
    version BIGINT NOT NULL DEFAULT 0,

    INDEX idx_products_sku (sku),
    INDEX idx_products_status (status),
    INDEX idx_products_category (category_id),
    INDEX idx_products_featured (featured),
    INDEX idx_products_name (name(100)),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Product images table
CREATE TABLE product_images (
    product_id BINARY(16) NOT NULL,
    image_url VARCHAR(500) NOT NULL,
    PRIMARY KEY (product_id, image_url),
    CONSTRAINT fk_product_images_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Product tags table
CREATE TABLE product_tags (
    product_id BINARY(16) NOT NULL,
    tag VARCHAR(50) NOT NULL,
    PRIMARY KEY (product_id, tag),
    INDEX idx_product_tags_tag (tag),
    CONSTRAINT fk_product_tags_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
