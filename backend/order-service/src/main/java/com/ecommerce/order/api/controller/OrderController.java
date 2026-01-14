package com.ecommerce.order.api.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.common.response.PageResponse;
import com.ecommerce.order.application.dto.request.CreateOrderRequest;
import com.ecommerce.order.application.dto.response.OrderResponse;
import com.ecommerce.order.application.usecase.CancelOrderUseCase;
import com.ecommerce.order.application.usecase.CreateOrderUseCase;
import com.ecommerce.order.application.usecase.GetOrderUseCase;
import com.ecommerce.order.application.usecase.UpdateOrderStatusUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for order management.
 * Delegates to use cases for business logic.
 */
@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class OrderController {

    private final CreateOrderUseCase createOrderUseCase;
    private final GetOrderUseCase getOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final UpdateOrderStatusUseCase updateOrderStatusUseCase;

    @PostMapping
    @Operation(summary = "Create a new order")
    public ResponseEntity<ApiResponse<OrderResponse>> createOrder(
            @AuthenticationPrincipal UUID userId,  // TODO: Replace with actual UserPrincipal
            @Valid @RequestBody CreateOrderRequest request) {
        OrderResponse response = createOrderUseCase.execute(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(@PathVariable UUID id) {
        OrderResponse response = getOrderUseCase.getById(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/number/{orderNumber}")
    @Operation(summary = "Get order by order number")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrderByNumber(@PathVariable String orderNumber) {
        OrderResponse response = getOrderUseCase.getByOrderNumber(orderNumber);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/my-orders")
    @Operation(summary = "List current user's orders")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> getMyOrders(
            @AuthenticationPrincipal UUID userId,  // TODO: Replace with actual UserPrincipal
            @PageableDefault(size = 20) Pageable pageable) {
        Page<OrderResponse> page = getOrderUseCase.listByUser(userId, pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "List all orders (Admin only)")
    public ResponseEntity<ApiResponse<PageResponse<OrderResponse>>> listAllOrders(
            @PageableDefault(size = 20) Pageable pageable) {
        Page<OrderResponse> page = getOrderUseCase.listAll(pageable);
        return ResponseEntity.ok(ApiResponse.success(PageResponse.from(page)));
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel an order")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable UUID id,
            @RequestParam(required = false, defaultValue = "Cancelled by customer") String reason) {
        OrderResponse response = cancelOrderUseCase.execute(id, reason);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    // ========== Admin Operations ==========

    @PostMapping("/{id}/confirm")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Confirm order (Admin only)", description = "Called after payment success")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(
            @PathVariable UUID id,
            @RequestParam UUID paymentId) {
        OrderResponse response = updateOrderStatusUseCase.confirmOrder(id, paymentId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/process")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Start processing order (Admin only)")
    public ResponseEntity<ApiResponse<OrderResponse>> processOrder(@PathVariable UUID id) {
        OrderResponse response = updateOrderStatusUseCase.startProcessing(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/ship")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Mark order as shipped (Admin only)")
    public ResponseEntity<ApiResponse<OrderResponse>> shipOrder(@PathVariable UUID id) {
        OrderResponse response = updateOrderStatusUseCase.shipOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/{id}/deliver")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Mark order as delivered (Admin only)")
    public ResponseEntity<ApiResponse<OrderResponse>> deliverOrder(@PathVariable UUID id) {
        OrderResponse response = updateOrderStatusUseCase.deliverOrder(id);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}
