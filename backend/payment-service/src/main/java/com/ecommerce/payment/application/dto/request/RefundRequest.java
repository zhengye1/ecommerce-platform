package com.ecommerce.payment.application.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Request DTO for processing a refund.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefundRequest {

    @NotNull(message = "Refund amount is required")
    @Positive(message = "Refund amount must be positive")
    private BigDecimal amount;

    private String reason;
}
