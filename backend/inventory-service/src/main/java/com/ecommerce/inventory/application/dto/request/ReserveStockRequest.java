package com.ecommerce.inventory.application.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveStockRequest {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotEmpty(message = "Items are required")
    @Valid
    private List<ReservationItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservationItem {
        @NotNull
        private UUID productId;
        @NotNull
        private Integer quantity;
    }
}
