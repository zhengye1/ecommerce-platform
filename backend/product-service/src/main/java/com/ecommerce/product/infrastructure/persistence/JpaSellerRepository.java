package com.ecommerce.product.infrastructure.persistence;

import com.ecommerce.product.domain.model.Seller;
import com.ecommerce.product.domain.model.SellerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaSellerRepository extends JpaRepository<Seller, UUID> {

    Optional<Seller> findByUserId(UUID userId);

    Optional<Seller> findByShopName(String shopName);

    List<Seller> findByStatus(SellerStatus status);

    boolean existsByUserId(UUID userId);

    boolean existsByShopName(String shopName);
}
