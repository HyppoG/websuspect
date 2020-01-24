package com.test.vulnerableapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.vulnerableapp.model.DeliveryDetails;

public interface DeliveryRepository extends JpaRepository<DeliveryDetails, Long> {
    Optional<DeliveryDetails> findById(long id);
    Optional<DeliveryDetails> findByOrderId(long id);

}
