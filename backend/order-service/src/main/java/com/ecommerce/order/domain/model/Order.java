package com.ecommerce.order.domain.model;

import com.ecommerce.common.domain.AggregateRoot;
import com.ecommerce.order.domain.event.OrderCreatedEvent;
import com.ecommerce.order.domain.event.OrderStatusChangedEvent;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Order aggregate root.
 */
@Entity
@Table(name = "orders")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Order extends AggregateRoot {

    @Column(name = "order_number", nullable = false, unique = true, length = 50)
    private String orderNumber;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "shipping_cost", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "currency", length = 3)
    @Builder.Default
    private String currency = "USD";

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "shipping_street")),
            @AttributeOverride(name = "city", column = @Column(name = "shipping_city")),
            @AttributeOverride(name = "state", column = @Column(name = "shipping_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "shipping_zip")),
            @AttributeOverride(name = "country", column = @Column(name = "shipping_country"))
    })
    private Address shippingAddress;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "billing_street")),
            @AttributeOverride(name = "city", column = @Column(name = "billing_city")),
            @AttributeOverride(name = "state", column = @Column(name = "billing_state")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "billing_zip")),
            @AttributeOverride(name = "country", column = @Column(name = "billing_country"))
    })
    private Address billingAddress;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<OrderItem> items = new ArrayList<>();

    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(length = 500)
    private String notes;

    @Column(name = "cancelled_at")
    private Instant cancelledAt;

    @Column(name = "cancellation_reason")
    private String cancellationReason;

    /**
     * Create a new order.
     */
    public static Order create(UUID userId, String orderNumber, List<OrderItem> items,
                                Address shippingAddress, Address billingAddress) {
        Order order = Order.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .orderNumber(orderNumber)
                .shippingAddress(shippingAddress)
                .billingAddress(billingAddress)
                .status(OrderStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        for (OrderItem item : items) {
            order.addItem(item);
        }

        order.calculateTotals();
        order.registerEvent(new OrderCreatedEvent(order.getId(), orderNumber, userId, order.getTotalAmount()));
        return order;
    }

    /**
     * Add item to order.
     */
    public void addItem(OrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }

    /**
     * Calculate order totals.
     */
    public void calculateTotals() {
        this.subtotal = items.stream()
                .map(OrderItem::getLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.totalAmount = subtotal
                .add(shippingCost != null ? shippingCost : BigDecimal.ZERO)
                .add(taxAmount != null ? taxAmount : BigDecimal.ZERO)
                .subtract(discountAmount != null ? discountAmount : BigDecimal.ZERO);
    }

    /**
     * Confirm the order (payment successful).
     */
    public void confirm(UUID paymentId) {
        if (this.status != OrderStatus.PENDING) {
            throw new IllegalStateException("Can only confirm pending orders");
        }
        this.paymentId = paymentId;
        updateStatus(OrderStatus.CONFIRMED);
    }

    /**
     * Mark order as processing.
     */
    public void startProcessing() {
        if (this.status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Can only process confirmed orders");
        }
        updateStatus(OrderStatus.PROCESSING);
    }

    /**
     * Mark order as shipped.
     */
    public void ship() {
        if (this.status != OrderStatus.PROCESSING) {
            throw new IllegalStateException("Can only ship processing orders");
        }
        updateStatus(OrderStatus.SHIPPED);
    }

    /**
     * Mark order as delivered.
     */
    public void deliver() {
        if (this.status != OrderStatus.SHIPPED) {
            throw new IllegalStateException("Can only deliver shipped orders");
        }
        updateStatus(OrderStatus.DELIVERED);
    }

    /**
     * Cancel the order.
     */
    public void cancel(String reason) {
        if (!canBeCancelled()) {
            throw new IllegalStateException("Order cannot be cancelled in current status: " + status);
        }
        this.cancellationReason = reason;
        this.cancelledAt = Instant.now();
        updateStatus(OrderStatus.CANCELLED);
    }

    /**
     * Check if order can be cancelled.
     */
    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING ||
               status == OrderStatus.CONFIRMED ||
               status == OrderStatus.PROCESSING;
    }

    private void updateStatus(OrderStatus newStatus) {
        OrderStatus previousStatus = this.status;
        this.status = newStatus;
        registerEvent(new OrderStatusChangedEvent(this.getId(), this.orderNumber, previousStatus, newStatus));
    }
}
