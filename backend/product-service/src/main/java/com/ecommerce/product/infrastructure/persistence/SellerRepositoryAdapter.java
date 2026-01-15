package com.ecommerce.product.infrastructure.persistence;

import com.ecommerce.product.domain.model.Seller;
import com.ecommerce.product.domain.model.SellerStatus;
import com.ecommerce.product.domain.repository.SellerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class SellerRepositoryAdapter implements SellerRepository {

    private final JpaSellerRepository jpaRepository;

    @Override
    public Seller save(Seller seller) {
        return jpaRepository.save(seller);
    }

    @Override
    public Optional<Seller> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Seller> findByUserId(UUID userId) {
        return jpaRepository.findByUserId(userId);
    }

    @Override
    public Optional<Seller> findByShopName(String shopName) {
        return jpaRepository.findByShopName(shopName);
    }

    @Override
    public List<Seller> findByStatus(SellerStatus status) {
        return jpaRepository.findByStatus(status);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        return jpaRepository.existsByUserId(userId);
    }

    @Override
    public boolean existsByShopName(String shopName) {
        return jpaRepository.existsByShopName(shopName);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
