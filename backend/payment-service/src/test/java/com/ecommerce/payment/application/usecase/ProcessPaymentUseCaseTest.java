package com.ecommerce.payment.application.usecase;

import com.ecommerce.common.test.base.BaseUnitTest;
import com.ecommerce.payment.application.dto.request.ProcessPaymentRequest;
import com.ecommerce.payment.application.dto.response.PaymentResponse;
import com.ecommerce.payment.application.mapper.PaymentMapper;
import com.ecommerce.payment.application.usecase.impl.ProcessPaymentUseCaseImpl;
import com.ecommerce.payment.domain.model.Payment;
import com.ecommerce.payment.domain.model.PaymentMethod;
import com.ecommerce.payment.domain.model.PaymentStatus;
import com.ecommerce.payment.domain.repository.PaymentRepository;
import com.ecommerce.payment.domain.service.PaymentProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ProcessPaymentUseCase Tests")
class ProcessPaymentUseCaseTest extends BaseUnitTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    @Mock
    private PaymentProvider paymentProvider;

    private ProcessPaymentUseCaseImpl processPaymentUseCase;

    private ProcessPaymentRequest request;
    private Payment savedPayment;
    private PaymentResponse expectedResponse;
    private UUID userId;

    @BeforeEach
    void setUp() {
        // Manually construct with List of providers
        processPaymentUseCase = new ProcessPaymentUseCaseImpl(
                paymentRepository,
                paymentMapper,
                List.of(paymentProvider)
        );

        userId = UUID.randomUUID();

        request = new ProcessPaymentRequest();
        request.setOrderId(UUID.randomUUID());
        request.setAmount(new BigDecimal("199.99"));
        request.setCurrency("USD");
        request.setPaymentMethod(PaymentMethod.CREDIT_CARD);
        request.setPaymentToken("tok_test_123");

        savedPayment = Payment.builder()
                .id(UUID.randomUUID())
                .orderId(request.getOrderId())
                .userId(userId)
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .status(PaymentStatus.PENDING)
                .build();

        expectedResponse = new PaymentResponse();
        expectedResponse.setId(savedPayment.getId());
        expectedResponse.setStatus(PaymentStatus.PENDING);

        // Mock provider supports CREDIT_CARD
        when(paymentProvider.supports(PaymentMethod.CREDIT_CARD)).thenReturn(true);
    }

    @Test
    @DisplayName("should create payment with PENDING status initially")
    void shouldCreatePaymentWithPendingStatus() {
        // Given
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(paymentMapper.toResponse(any(Payment.class))).thenReturn(expectedResponse);

        // When
        PaymentResponse result = processPaymentUseCase.execute(userId, request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(PaymentStatus.PENDING);

        verify(paymentRepository, atLeastOnce()).save(any(Payment.class));
    }

    @Test
    @DisplayName("should save payment with correct amount and currency")
    void shouldSavePaymentWithCorrectDetails() {
        // Given
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment payment = inv.getArgument(0);
            assertThat(payment.getAmount()).isEqualByComparingTo(new BigDecimal("199.99"));
            assertThat(payment.getCurrency()).isEqualTo("USD");
            return savedPayment;
        });
        when(paymentMapper.toResponse(any(Payment.class))).thenReturn(expectedResponse);

        // When
        processPaymentUseCase.execute(userId, request);

        // Then
        verify(paymentRepository, atLeastOnce()).save(any(Payment.class));
    }
}
