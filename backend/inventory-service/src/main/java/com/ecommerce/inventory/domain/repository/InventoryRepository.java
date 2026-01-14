package com.ecommerce.inventory.domain.repository;

import com.ecommerce.inventory.domain.model.InventoryItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InventoryRepository {

    InventoryItem save(InventoryItem item);

    Optional<InventoryItem> findById(UUID id);

    Optional<InventoryItem> findByProductId(UUID productId);

    Optional<InventoryItem> findBySku(String sku);

    List<InventoryItem> findByProductIdIn(List<UUID> productIds);

    List<InventoryItem> findLowStock();

    boolean existsByProductId(UUID productId);

    void deleteById(UUID id);
}
