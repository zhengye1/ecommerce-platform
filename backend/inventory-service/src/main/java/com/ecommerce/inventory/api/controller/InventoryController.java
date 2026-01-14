package com.ecommerce.inventory.api.controller;

import com.ecommerce.common.response.ApiResponse;
import com.ecommerce.inventory.application.dto.request.ReserveStockRequest;
import com.ecommerce.inventory.application.dto.response.InventoryResponse;
import com.ecommerce.inventory.application.dto.response.ReservationResponse;
import com.ecommerce.inventory.application.usecase.ReserveStockUseCase;
import com.ecommerce.inventory.domain.model.InventoryItem;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/inventory")
@RequiredArgsConstructor
@Tag(name = "Inventory", description = "Inventory management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class InventoryController {

    private final ReserveStockUseCase reserveStockUseCase;
    private final InventoryRepository inventoryRepository;
    // TODO: Add other use cases (GetInventory, UpdateStock, ReleaseReservation)

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get inventory for a product")
    public ResponseEntity<ApiResponse<InventoryResponse>> getInventory(@PathVariable UUID productId) {
        // TODO: Move to GetInventoryUseCase
        InventoryItem item = inventoryRepository.findByProductId(productId)
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        InventoryResponse response = InventoryResponse.builder()
                .productId(item.getProductId())
                .sku(item.getSku())
                .quantityAvailable(item.getQuantityAvailable())
                .quantityReserved(item.getQuantityReserved())
                .inStock(item.isInStock())
                .lowStock(item.needsReorder())
                .build();

        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/reserve")
    @Operation(summary = "Reserve stock for an order")
    public ResponseEntity<ApiResponse<ReservationResponse>> reserveStock(
            @Valid @RequestBody ReserveStockRequest request) {
        ReservationResponse response = reserveStockUseCase.execute(request);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PostMapping("/release/{orderId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SYSTEM')")
    @Operation(summary = "Release stock reservation (Admin/System only)")
    public ResponseEntity<ApiResponse<Void>> releaseReservation(@PathVariable UUID orderId) {
        // TODO: Implement ReleaseReservationUseCase
        return ResponseEntity.ok(ApiResponse.success(null));
    }

    @PutMapping("/product/{productId}/stock")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update stock level (Admin only)")
    public ResponseEntity<ApiResponse<InventoryResponse>> updateStock(
            @PathVariable UUID productId,
            @RequestParam int quantity) {
        // TODO: Implement UpdateStockUseCase
        return ResponseEntity.ok(ApiResponse.success(null));
    }
}
