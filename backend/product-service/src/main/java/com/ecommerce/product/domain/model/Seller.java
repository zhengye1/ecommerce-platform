package com.ecommerce.product.domain.model;

import com.ecommerce.common.domain.AggregateRoot;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Seller aggregate root.
 * Represents a seller/merchant in the marketplace.
 */
@Entity
@Table(name = "sellers")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Seller extends AggregateRoot {

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(name = "shop_name", nullable = false, length = 100)
    private String shopName;

    @Column(name = "shop_description", columnDefinition = "TEXT")
    private String shopDescription;

    @Column(name = "logo_url")
    private String logoUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private SellerStatus status = SellerStatus.PENDING;

    @Column(name = "commission_rate", precision = 5, scale = 4)
    @Builder.Default
    private BigDecimal commissionRate = new BigDecimal("0.10"); // 10% default

    @Column(name = "business_license", length = 100)
    private String businessLicense;

    @Column(name = "contact_email")
    private String contactEmail;

    @Column(name = "contact_phone", length = 20)
    private String contactPhone;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Column(name = "rating", precision = 3, scale = 2)
    @Builder.Default
    private BigDecimal rating = BigDecimal.ZERO;

    @Column(name = "review_count")
    @Builder.Default
    private Integer reviewCount = 0;

    @Column(name = "total_sales")
    @Builder.Default
    private Integer totalSales = 0;

    /**
     * Create a new seller registration.
     */
    public static Seller create(UUID userId, String shopName, String contactEmail) {
        return Seller.builder()
                .userId(userId)
                .shopName(shopName)
                .contactEmail(contactEmail)
                .status(SellerStatus.PENDING)
                .build();
    }

    /**
     * Verify the seller.
     */
    public void verify() {
        this.status = SellerStatus.VERIFIED;
        this.verifiedAt = Instant.now();
    }

    /**
     * Suspend the seller.
     */
    public void suspend() {
        this.status = SellerStatus.SUSPENDED;
    }

    /**
     * Check if seller is active and can list products.
     */
    public boolean canListProducts() {
        return status == SellerStatus.VERIFIED;
    }

    /**
     * Update rating based on new review.
     */
    public void updateRating(BigDecimal newRating) {
        // Calculate new average rating
        BigDecimal totalRating = this.rating.multiply(BigDecimal.valueOf(this.reviewCount));
        this.reviewCount++;
        this.rating = totalRating.add(newRating).divide(BigDecimal.valueOf(this.reviewCount), 2, java.math.RoundingMode.HALF_UP);
    }

    /**
     * Increment total sales count.
     */
    public void incrementSales() {
        this.totalSales++;
    }
}
