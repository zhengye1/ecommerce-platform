package com.ecommerce.order.application.dto.response;

import com.ecommerce.order.domain.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for order.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private UUID id;
    private String orderNumber;
    private UUID userId;
    private OrderStatus status;

    private BigDecimal subtotal;
    private BigDecimal shippingCost;
    private BigDecimal taxAmount;
    private BigDecimal discountAmount;
    private BigDecimal totalAmount;
    private String currency;

    private AddressResponse shippingAddress;
    private AddressResponse billingAddress;

    private List<OrderItemResponse> items;

    private UUID paymentId;
    private String notes;

    private Instant cancelledAt;
    private String cancellationReason;

    private Instant createdAt;
    private Instant updatedAt;
}
