package com.ecommerce.inventory.infrastructure.persistence;

import com.ecommerce.inventory.domain.model.InventoryItem;
import com.ecommerce.inventory.domain.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class InventoryRepositoryAdapter implements InventoryRepository {

    private final JpaInventoryRepository jpaRepository;

    @Override
    public InventoryItem save(InventoryItem item) {
        return jpaRepository.save(item);
    }

    @Override
    public Optional<InventoryItem> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<InventoryItem> findByProductId(UUID productId) {
        return jpaRepository.findByProductId(productId);
    }

    @Override
    public Optional<InventoryItem> findBySku(String sku) {
        return jpaRepository.findBySku(sku);
    }

    @Override
    public List<InventoryItem> findByProductIdIn(List<UUID> productIds) {
        return jpaRepository.findByProductIdIn(productIds);
    }

    @Override
    public List<InventoryItem> findLowStock() {
        return jpaRepository.findLowStock();
    }

    @Override
    public boolean existsByProductId(UUID productId) {
        return jpaRepository.existsByProductId(productId);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
