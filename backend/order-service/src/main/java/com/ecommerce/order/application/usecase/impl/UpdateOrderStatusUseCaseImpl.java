package com.ecommerce.order.application.usecase.impl;

import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.usecase.UpdateOrderStatusUseCase;
import com.ecommerce.order.domain.exception.OrderNotFoundException;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of UpdateOrderStatusUseCase.
 *
 * Handles order status transitions as part of the order lifecycle:
 * PENDING -> CONFIRMED -> PROCESSING -> SHIPPED -> DELIVERED
 *
 * TODO: Implement the following business logic:
 * - Add validation for status transitions
 * - Trigger notifications on status change
 * - Update shipment tracking info when shipping
 * - Handle edge cases (concurrent updates, etc.)
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UpdateOrderStatusUseCaseImpl implements UpdateOrderStatusUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    // TODO: Inject NotificationService for status update emails

    @Override
    public OrderResponse confirmOrder(UUID orderId, UUID paymentId) {
        log.info("Confirming order: {} with payment: {}", orderId, paymentId);

        Order order = findOrder(orderId);
        order.confirm(paymentId);

        // TODO: Notify inventory-service to confirm reservation
        // TODO: Send order confirmation email to customer

        return saveAndMap(order);
    }

    @Override
    public OrderResponse startProcessing(UUID orderId) {
        log.info("Starting processing for order: {}", orderId);

        Order order = findOrder(orderId);
        order.startProcessing();

        // TODO: Notify fulfillment system
        // TODO: Send "order being prepared" email

        return saveAndMap(order);
    }

    @Override
    public OrderResponse shipOrder(UUID orderId) {
        log.info("Shipping order: {}", orderId);

        Order order = findOrder(orderId);
        order.ship();

        // TODO: Add shipping carrier and tracking number
        // TODO: Notify inventory-service to deduct stock (if not done earlier)
        // TODO: Send shipment notification with tracking link

        return saveAndMap(order);
    }

    @Override
    public OrderResponse deliverOrder(UUID orderId) {
        log.info("Marking order as delivered: {}", orderId);

        Order order = findOrder(orderId);
        order.deliver();

        // TODO: Send delivery confirmation email
        // TODO: Trigger review request email (after X days)

        return saveAndMap(order);
    }

    private Order findOrder(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
    }

    private OrderResponse saveAndMap(Order order) {
        Order saved = orderRepository.save(order);
        return orderMapper.toResponse(saved);
    }
}
