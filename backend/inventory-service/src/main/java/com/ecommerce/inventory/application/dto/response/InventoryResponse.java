package com.ecommerce.inventory.application.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponse {

    private UUID productId;
    private String sku;
    private int quantityAvailable;
    private int quantityReserved;
    private boolean inStock;
    private boolean lowStock;
    private String warehouseLocation;
}
