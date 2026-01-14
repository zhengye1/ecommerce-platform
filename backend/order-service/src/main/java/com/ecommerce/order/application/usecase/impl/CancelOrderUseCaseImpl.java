package com.ecommerce.order.application.usecase.impl;

import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.usecase.CancelOrderUseCase;
import com.ecommerce.order.domain.exception.InvalidOrderStatusException;
import com.ecommerce.order.domain.exception.OrderNotFoundException;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of CancelOrderUseCase.
 *
 * This use case handles order cancellation and triggers saga compensation:
 * 1. Validate order can be cancelled
 * 2. Cancel the order
 * 3. Publish OrderCancelledEvent
 * 4. Saga compensation: refund payment, release inventory
 *
 * TODO: Implement the following business logic:
 * - Verify user has permission to cancel (owner or admin)
 * - Check cancellation policy (time window, order status)
 * - Trigger refund via payment-service
 * - Release inventory reservation via inventory-service
 * - Send cancellation notification
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CancelOrderUseCaseImpl implements CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    // TODO: Inject PaymentServiceClient for refund
    // TODO: Inject InventoryServiceClient to release reservation
    // TODO: Inject NotificationService to send cancellation email

    @Override
    public OrderResponse execute(UUID orderId, String reason) {
        log.info("Cancelling order: {} with reason: {}", orderId, reason);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));

        // TODO: Verify current user owns this order or is admin

        // Check if order can be cancelled
        if (!order.canBeCancelled()) {
            throw new InvalidOrderStatusException(order.getStatus(), "cancel");
        }

        // TODO: Check cancellation policy
        // - Is order within cancellation time window?
        // - Are there any restrictions based on order status?

        // Cancel the order (this registers OrderStatusChangedEvent)
        order.cancel(reason);

        // TODO: Saga compensation step 1 - Refund payment if already processed
        // if (order.getPaymentId() != null) {
        //     paymentServiceClient.refund(order.getPaymentId(), order.getTotalAmount());
        // }

        // TODO: Saga compensation step 2 - Release inventory reservation
        // inventoryServiceClient.releaseReservation(order.getOrderNumber());

        // TODO: Send cancellation notification
        // notificationService.sendOrderCancellation(order);

        Order savedOrder = orderRepository.save(order);
        log.info("Order cancelled: {}", orderId);

        return orderMapper.toResponse(savedOrder);
    }
}
