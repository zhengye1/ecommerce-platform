package com.ecommerce.product.domain.model;

import com.ecommerce.common.domain.AggregateRoot;
import com.ecommerce.product.domain.event.ProductCreatedEvent;
import com.ecommerce.product.domain.event.ProductUpdatedEvent;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Product aggregate root.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Product extends AggregateRoot {

    @Column(nullable = false, unique = true, length = 100)
    private String sku;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "compare_at_price", precision = 10, scale = 2)
    private BigDecimal compareAtPrice;

    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "seller_id", nullable = false)
    private UUID sellerId;

    @Column(name = "brand")
    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ProductStatus status = ProductStatus.DRAFT;

    @Column(name = "stock_quantity", nullable = false)
    @Builder.Default
    private Integer stockQuantity = 0;

    @Column(name = "low_stock_threshold")
    @Builder.Default
    private Integer lowStockThreshold = 10;

    @Column(nullable = false)
    @Builder.Default
    private Boolean featured = false;

    @Column(name = "weight_kg", precision = 10, scale = 3)
    private BigDecimal weightKg;

    @ElementCollection
    @CollectionTable(name = "product_images", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "image_url")
    @Builder.Default
    private Set<String> imageUrls = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "product_tags", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tag")
    @Builder.Default
    private Set<String> tags = new HashSet<>();

    /**
     * Create a new product.
     */
    public static Product create(String sku, String name, String description,
                                  BigDecimal price, Category category, UUID sellerId) {
        Product product = Product.builder()
                .id(UUID.randomUUID())
                .sku(sku)
                .name(name)
                .description(description)
                .price(price)
                .category(category)
                .sellerId(sellerId)
                .status(ProductStatus.DRAFT)
                .build();

        product.registerEvent(new ProductCreatedEvent(product.getId(), sku, name, price));
        return product;
    }

    /**
     * Update product details.
     */
    public void updateDetails(String name, String description, BigDecimal price) {
        this.name = name;
        this.description = description;
        this.price = price;
        registerEvent(new ProductUpdatedEvent(this.getId(), this.sku, name, price));
    }

    /**
     * Publish the product (make it active).
     */
    public void publish() {
        if (this.status == ProductStatus.DRAFT || this.status == ProductStatus.INACTIVE) {
            this.status = ProductStatus.ACTIVE;
        }
    }

    /**
     * Deactivate the product.
     */
    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
    }

    /**
     * Mark product as out of stock.
     */
    public void markOutOfStock() {
        this.status = ProductStatus.OUT_OF_STOCK;
        this.stockQuantity = 0;
    }

    /**
     * Update stock quantity.
     */
    public void updateStock(int quantity) {
        this.stockQuantity = quantity;
        if (quantity <= 0) {
            this.status = ProductStatus.OUT_OF_STOCK;
        } else if (this.status == ProductStatus.OUT_OF_STOCK) {
            this.status = ProductStatus.ACTIVE;
        }
    }

    /**
     * Check if product is low on stock.
     */
    public boolean isLowStock() {
        return stockQuantity != null && lowStockThreshold != null
                && stockQuantity <= lowStockThreshold;
    }

    /**
     * Add an image URL.
     */
    public void addImage(String imageUrl) {
        this.imageUrls.add(imageUrl);
    }

    /**
     * Add a tag.
     */
    public void addTag(String tag) {
        this.tags.add(tag.toLowerCase());
    }
}
