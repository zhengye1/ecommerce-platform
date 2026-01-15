package com.ecommerce.inventory.application.usecase;

import com.ecommerce.common.test.base.BaseUnitTest;
import com.ecommerce.inventory.application.dto.request.ReserveStockRequest;
import com.ecommerce.inventory.application.dto.response.ReservationResponse;
import com.ecommerce.inventory.application.usecase.impl.ReserveStockUseCaseImpl;
import com.ecommerce.inventory.domain.model.InventoryItem;
import com.ecommerce.inventory.domain.model.StockReservation;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import com.ecommerce.inventory.domain.repository.StockReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayName("ReserveStockUseCase Tests")
class ReserveStockUseCaseTest extends BaseUnitTest {

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private StockReservationRepository stockReservationRepository;

    @InjectMocks
    private ReserveStockUseCaseImpl reserveStockUseCase;

    private ReserveStockRequest request;
    private UUID productId;
    private UUID orderId;
    private InventoryItem inventoryItem;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        ReserveStockRequest.ReservationItem itemRequest = ReserveStockRequest.ReservationItem.builder()
                .productId(productId)
                .quantity(5)
                .build();

        request = ReserveStockRequest.builder()
                .orderId(orderId)
                .items(List.of(itemRequest))
                .build();

        inventoryItem = InventoryItem.builder()
                .productId(productId)
                .sku("TEST-SKU-001")
                .quantityAvailable(100)
                .quantityReserved(10)
                .build();
        inventoryItem.setId(UUID.randomUUID());
    }

    @Test
    @DisplayName("should reserve stock successfully when sufficient quantity available")
    void shouldReserveStockSuccessfully() {
        // Given
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventoryItem));
        when(inventoryRepository.save(any(InventoryItem.class))).thenAnswer(inv -> inv.getArgument(0));
        when(stockReservationRepository.save(any(StockReservation.class))).thenAnswer(inv -> inv.getArgument(0));

        // When
        ReservationResponse result = reserveStockUseCase.execute(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isTrue();
        assertThat(result.getItems()).hasSize(1);

        verify(inventoryRepository).findByProductId(productId);
        verify(inventoryRepository).save(any(InventoryItem.class));
        verify(stockReservationRepository).save(any(StockReservation.class));
    }

    @Test
    @DisplayName("should fail reservation when insufficient stock")
    void shouldFailWhenInsufficientStock() {
        // Given
        InventoryItem lowStockItem = InventoryItem.builder()
                .productId(productId)
                .quantityAvailable(3)
                .quantityReserved(0)
                .build();
        lowStockItem.setId(UUID.randomUUID());
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(lowStockItem));

        // When
        ReservationResponse result = reserveStockUseCase.execute(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isFalse();

        verify(stockReservationRepository, never()).save(any());
    }

    @Test
    @DisplayName("should fail reservation when product not found in inventory")
    void shouldFailWhenProductNotFound() {
        // Given
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.empty());

        // When
        ReservationResponse result = reserveStockUseCase.execute(request);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.isSuccess()).isFalse();
    }
}
