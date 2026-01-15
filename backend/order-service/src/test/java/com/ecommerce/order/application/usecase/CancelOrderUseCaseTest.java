package com.ecommerce.order.application.usecase;

import com.ecommerce.common.exception.ResourceNotFoundException;
import com.ecommerce.common.test.base.BaseUnitTest;
import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.usecase.impl.CancelOrderUseCaseImpl;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import com.ecommerce.order.domain.exception.InvalidOrderStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CancelOrderUseCase Tests")
class CancelOrderUseCaseTest extends BaseUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private CancelOrderUseCaseImpl cancelOrderUseCase;

    private UUID orderId;
    private Order pendingOrder;
    private OrderResponse cancelledResponse;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();

        pendingOrder = Order.builder()
                .id(orderId)
                .userId(UUID.randomUUID())
                .orderNumber("ORD-20240101-001")
                .status(OrderStatus.PENDING)
                .totalAmount(new BigDecimal("199.99"))
                .build();

        cancelledResponse = new OrderResponse();
        cancelledResponse.setId(orderId);
        cancelledResponse.setStatus(OrderStatus.CANCELLED);
    }

    @Test
    @DisplayName("should cancel pending order successfully")
    void shouldCancelPendingOrderSuccessfully() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(pendingOrder));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));
        when(orderMapper.toResponse(any(Order.class))).thenReturn(cancelledResponse);

        // When
        OrderResponse result = cancelOrderUseCase.execute(orderId, "Customer requested cancellation");

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.CANCELLED);

        verify(orderRepository).findById(orderId);
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("should throw exception when order not found")
    void shouldThrowExceptionWhenOrderNotFound() {
        // Given
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> cancelOrderUseCase.execute(orderId, "reason"))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(orderRepository, never()).save(any());
    }

    @Test
    @DisplayName("should throw exception when order already shipped")
    void shouldThrowExceptionWhenOrderAlreadyShipped() {
        // Given
        Order shippedOrder = Order.builder()
                .id(orderId)
                .status(OrderStatus.SHIPPED)
                .build();
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(shippedOrder));

        // When & Then
        assertThatThrownBy(() -> cancelOrderUseCase.execute(orderId, "reason"))
                .isInstanceOf(InvalidOrderStatusException.class);
    }
}
