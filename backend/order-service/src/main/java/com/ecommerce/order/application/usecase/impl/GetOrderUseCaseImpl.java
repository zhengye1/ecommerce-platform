package com.ecommerce.order.application.usecase.impl;

import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.usecase.GetOrderUseCase;
import com.ecommerce.order.domain.exception.OrderNotFoundException;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Implementation of GetOrderUseCase.
 *
 * TODO: Implement the following business logic:
 * - Access control: users can only see their own orders
 * - Admins can see all orders
 * - Add caching for frequently accessed orders
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GetOrderUseCaseImpl implements GetOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public OrderResponse getById(UUID orderId) {
        // TODO: Add access control - verify current user owns this order or is admin
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toResponse(order);
    }

    @Override
    public OrderResponse getByOrderNumber(String orderNumber) {
        // TODO: Add access control
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new OrderNotFoundException(orderNumber));
        return orderMapper.toResponse(order);
    }

    @Override
    public Page<OrderResponse> listByUser(UUID userId, Pageable pageable) {
        // TODO: Verify current user matches userId or is admin
        return orderRepository.findByUserId(userId, pageable)
                .map(orderMapper::toResponse);
    }

    @Override
    public Page<OrderResponse> listAll(Pageable pageable) {
        // TODO: Verify current user is admin
        return orderRepository.findAll(pageable)
                .map(orderMapper::toResponse);
    }
}
