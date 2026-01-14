package com.ecommerce.inventory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {

    private UUID orderId;
    private boolean success;
    private String message;
    private List<ReservedItem> items;
    private Instant expiresAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReservedItem {
        private UUID productId;
        private int quantityReserved;
        private boolean reserved;
        private String failureReason;
    }
}
