package com.ecommerce.payment.domain.service;

import com.ecommerce.payment.domain.model.PaymentMethod;

import java.math.BigDecimal;

/**
 * Strategy interface for payment providers.
 * Implement this for each payment gateway (Stripe, PayPal, etc.)
 *
 * TODO: Implement concrete providers:
 * - StripePaymentProvider
 * - PayPalPaymentProvider
 * - etc.
 */
public interface PaymentProvider {

    /**
     * Get the name of this payment provider.
     */
    String getProviderName();

    /**
     * Check if this provider supports the given payment method.
     */
    boolean supports(PaymentMethod method);

    /**
     * Process a payment.
     *
     * @param amount the amount to charge
     * @param currency the currency code
     * @param paymentToken the payment token from frontend (e.g., Stripe token)
     * @return the provider's transaction ID
     * @throws PaymentProcessingException if payment fails
     */
    String processPayment(BigDecimal amount, String currency, String paymentToken);

    /**
     * Process a refund.
     *
     * @param transactionId the original transaction ID
     * @param amount the amount to refund
     * @return the refund transaction ID
     * @throws PaymentProcessingException if refund fails
     */
    String processRefund(String transactionId, BigDecimal amount);

    /**
     * Exception thrown when payment processing fails.
     */
    class PaymentProcessingException extends RuntimeException {
        public PaymentProcessingException(String message) {
            super(message);
        }

        public PaymentProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
