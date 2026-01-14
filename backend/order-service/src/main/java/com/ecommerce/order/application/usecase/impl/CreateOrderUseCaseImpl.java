package com.ecommerce.order.application.usecase.impl;

import com.ecommerce.order.application.dto.request.CreateOrderRequest;
import com.ecommerce.order.application.dto.request.OrderItemRequest;
import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.usecase.CreateOrderUseCase;
import com.ecommerce.order.domain.model.Address;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderItem;
import com.ecommerce.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Implementation of CreateOrderUseCase.
 *
 * This use case orchestrates the order creation process which involves:
 * 1. Validating the order request
 * 2. Fetching product details from product-service
 * 3. Reserving inventory from inventory-service
 * 4. Creating the order
 * 5. Publishing OrderCreatedEvent to start the saga
 *
 * TODO: Implement the following business logic:
 * - Fetch product details (name, price, availability) from product-service
 * - Reserve inventory via inventory-service (saga step 1)
 * - Apply coupon/discount if provided
 * - Calculate shipping cost based on address and items
 * - Calculate tax based on shipping address
 * - Implement compensation logic if any step fails (saga rollback)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CreateOrderUseCaseImpl implements CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    // TODO: Inject ProductServiceClient to fetch product details
    // TODO: Inject InventoryServiceClient to reserve inventory
    // TODO: Inject CouponService for discount calculation
    // TODO: Inject ShippingCalculator for shipping cost
    // TODO: Inject TaxCalculator for tax calculation

    @Override
    public OrderResponse execute(UUID userId, CreateOrderRequest request) {
        log.info("Creating order for user: {}", userId);

        // Generate unique order number
        String orderNumber = generateOrderNumber();

        // Convert addresses
        Address shippingAddress = orderMapper.toAddress(request.getShippingAddress());
        Address billingAddress = request.getBillingAddress() != null
                ? orderMapper.toAddress(request.getBillingAddress())
                : shippingAddress;

        // TODO: Step 1 - Fetch product details from product-service
        // For each item, get: productId, name, sku, current price, availability
        // Throw exception if any product is unavailable or inactive

        // Build order items
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequest itemRequest : request.getItems()) {
            // TODO: Replace with actual product data from product-service
            OrderItem item = OrderItem.builder()
                    .id(UUID.randomUUID())
                    .productId(itemRequest.getProductId())
                    .productName("TODO: Fetch from product-service")
                    .productSku("TODO: Fetch from product-service")
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(BigDecimal.ZERO) // TODO: Fetch from product-service
                    .build();
            item.calculateLineTotal();
            orderItems.add(item);
        }

        // TODO: Step 2 - Reserve inventory via inventory-service
        // Call inventory-service to reserve stock for each item
        // If reservation fails, throw exception (no order created)
        // Save reservation IDs for potential rollback

        // TODO: Step 3 - Apply coupon/discount if provided
        // if (request.getCouponCode() != null) {
        //     Discount discount = couponService.validate(request.getCouponCode(), orderItems);
        //     // Apply discount to order
        // }

        // TODO: Step 4 - Calculate shipping cost
        // BigDecimal shippingCost = shippingCalculator.calculate(shippingAddress, orderItems);

        // TODO: Step 5 - Calculate tax
        // BigDecimal taxAmount = taxCalculator.calculate(shippingAddress, subtotal);

        // Create order using factory method
        Order order = Order.create(userId, orderNumber, orderItems, shippingAddress, billingAddress);
        order.setNotes(request.getNotes());

        // Save order (this will publish OrderCreatedEvent via @DomainEvents)
        Order savedOrder = orderRepository.save(order);
        log.info("Order created: {} for user: {}", orderNumber, userId);

        // TODO: The OrderCreatedEvent will trigger the saga:
        // 1. Payment processing (payment-service listens)
        // 2. If payment succeeds -> order confirmed
        // 3. If payment fails -> release inventory reservation (compensation)

        return orderMapper.toResponse(savedOrder);
    }

    private String generateOrderNumber() {
        // Format: ORD-YYYYMMDD-XXXXX (e.g., ORD-20260113-A1B2C)
        // TODO: Implement proper order number generation with sequence or Redis counter
        String datePart = java.time.LocalDate.now().toString().replace("-", "");
        String randomPart = UUID.randomUUID().toString().substring(0, 5).toUpperCase();
        return "ORD-" + datePart + "-" + randomPart;
    }
}
