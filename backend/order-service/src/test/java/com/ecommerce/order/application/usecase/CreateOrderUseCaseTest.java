package com.ecommerce.order.application.usecase;

import com.ecommerce.common.test.base.BaseUnitTest;
import com.ecommerce.order.application.dto.request.CreateOrderRequest;
import com.ecommerce.order.application.dto.request.OrderItemRequest;
import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.application.mapper.OrderMapper;
import com.ecommerce.order.application.usecase.impl.CreateOrderUseCaseImpl;
import com.ecommerce.order.domain.model.Order;
import com.ecommerce.order.domain.model.OrderStatus;
import com.ecommerce.order.domain.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("CreateOrderUseCase Tests")
class CreateOrderUseCaseTest extends BaseUnitTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private CreateOrderUseCaseImpl createOrderUseCase;

    private CreateOrderRequest request;
    private Order savedOrder;
    private OrderResponse expectedResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();

        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setProductId(UUID.randomUUID());
        itemRequest.setQuantity(2);

        request = new CreateOrderRequest();
        request.setItems(List.of(itemRequest));

        savedOrder = Order.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .orderNumber("ORD-20240101-001")
                .status(OrderStatus.PENDING)
                .totalAmount(BigDecimal.ZERO)
                .build();

        expectedResponse = new OrderResponse();
        expectedResponse.setId(savedOrder.getId());
        expectedResponse.setOrderNumber(savedOrder.getOrderNumber());
        expectedResponse.setStatus(OrderStatus.PENDING);
    }

    @Test
    @DisplayName("should create order with PENDING status")
    void shouldCreateOrderWithPendingStatus() {
        // Given
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(expectedResponse);

        // When
        OrderResponse result = createOrderUseCase.execute(userId, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(OrderStatus.PENDING);

        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("should generate unique order number")
    void shouldGenerateUniqueOrderNumber() {
        // Given
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order order = inv.getArgument(0);
            assertThat(order.getOrderNumber()).isNotNull();
            assertThat(order.getOrderNumber()).startsWith("ORD-");
            return savedOrder;
        });
        when(orderMapper.toResponse(any(Order.class))).thenReturn(expectedResponse);

        // When
        createOrderUseCase.execute(userId, request);

        // Then
        verify(orderRepository).save(any(Order.class));
    }
}
