package com.ecommerce.product.domain.repository;

import com.ecommerce.product.domain.model.Seller;
import com.ecommerce.product.domain.model.SellerStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository port for Seller aggregate.
 */
public interface SellerRepository {

    Seller save(Seller seller);

    Optional<Seller> findById(UUID id);

    Optional<Seller> findByUserId(UUID userId);

    Optional<Seller> findByShopName(String shopName);

    List<Seller> findByStatus(SellerStatus status);

    boolean existsByUserId(UUID userId);

    boolean existsByShopName(String shopName);

    void deleteById(UUID id);
}
