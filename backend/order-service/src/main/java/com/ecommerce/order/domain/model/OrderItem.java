package com.ecommerce.order.domain.model;

import com.ecommerce.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Order line item entity.
 */
@Entity
@Table(name = "order_items")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Column(name = "product_sku", nullable = false, length = 100)
    private String productSku;

    @Column(name = "product_name", nullable = false)
    private String productName;

    @Column(name = "product_image_url", length = 500)
    private String productImageUrl;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "line_total", nullable = false, precision = 12, scale = 2)
    private BigDecimal lineTotal;

    /**
     * Create a new order item.
     */
    public static OrderItem create(UUID productId, String sku, String name,
                                    String imageUrl, int quantity, BigDecimal unitPrice) {
        OrderItem item = OrderItem.builder()
                .id(UUID.randomUUID())
                .productId(productId)
                .productSku(sku)
                .productName(name)
                .productImageUrl(imageUrl)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build();
        item.calculateLineTotal();
        return item;
    }

    /**
     * Calculate line total.
     */
    public void calculateLineTotal() {
        this.lineTotal = unitPrice.multiply(BigDecimal.valueOf(quantity));
    }

    /**
     * Update quantity and recalculate.
     */
    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        calculateLineTotal();
    }
}
