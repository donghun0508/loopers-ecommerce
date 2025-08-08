package com.loopers.domain.catalog;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends Repository<Product, Long> {

    Product save(Product product);

    boolean existsById(Long targetId);

    List<Product> findAllByIdIn(List<Long> productIds);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p WHERE p.id IN :productIds")
    List<Product> findAllByIdInWithLock(@Param("productIds") List<Long> productIds);

    Optional<Product> findById(Long productId);
}
