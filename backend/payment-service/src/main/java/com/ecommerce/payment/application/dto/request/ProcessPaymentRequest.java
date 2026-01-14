package com.ecommerce.payment.application.dto.request;

import com.ecommerce.payment.domain.model.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Request DTO for processing a payment.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPaymentRequest {

    @NotNull(message = "Order ID is required")
    private UUID orderId;

    @NotNull(message = "Amount is required")
    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String currency;

    @NotNull(message = "Payment method is required")
    private PaymentMethod paymentMethod;

    @NotBlank(message = "Payment token is required")
    private String paymentToken;
}
