package com.ecommerce.inventory.infrastructure.persistence;

import com.ecommerce.inventory.domain.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaInventoryRepository extends JpaRepository<InventoryItem, UUID> {

    Optional<InventoryItem> findByProductId(UUID productId);

    Optional<InventoryItem> findBySku(String sku);

    List<InventoryItem> findByProductIdIn(List<UUID> productIds);

    @Query("SELECT i FROM InventoryItem i WHERE (i.quantityAvailable + i.quantityReserved) <= i.reorderLevel")
    List<InventoryItem> findLowStock();

    boolean existsByProductId(UUID productId);
}
