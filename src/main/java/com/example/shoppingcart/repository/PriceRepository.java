package com.example.shoppingcart.repository;

import com.example.shoppingcart.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PriceRepository extends JpaRepository<PriceEntity, Long> {

    @Query("""
        SELECT p FROM PriceEntity p
        WHERE p.productType = :productType
          AND p.clientType = :clientType
          AND p.revenueThreshold <= :revenue
        ORDER BY p.revenueThreshold DESC
        """)
    Optional<PriceEntity> findBestPrice(
            @Param("productType") String productType,
            @Param("clientType") String clientType,
            @Param("revenue") Long revenue
    );
}
