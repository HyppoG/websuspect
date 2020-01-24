package com.test.vulnerableapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.UserInfo;

public interface OrderRepository extends JpaRepository<OrderDetails, Long> {
    Optional<OrderDetails> findById(long id);
}
