package com.test.vulnerableapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.vulnerableapp.model.PaymentDetails;

public interface PaymentRepository extends JpaRepository<PaymentDetails, Long>, PaymentRepositoryCustom {
    Optional<PaymentDetails> findById(long id);
    Optional<PaymentDetails> findByOrderId(long id);
    boolean savePaymentDetails(PaymentDetails paymentDetails);
}
