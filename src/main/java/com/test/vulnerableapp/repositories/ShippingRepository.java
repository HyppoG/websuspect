package com.test.vulnerableapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.vulnerableapp.model.ShippingDetails;

public interface ShippingRepository extends JpaRepository<ShippingDetails, Long> {
    Optional<ShippingDetails> findById(long id);
    Optional<ShippingDetails> findByOrderId(long id);
}
