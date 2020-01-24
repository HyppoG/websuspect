package com.test.vulnerableapp.repositories;

import com.test.vulnerableapp.model.OrderedFlightInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderedFlightInfoRepository extends JpaRepository<OrderedFlightInfo, Long> {
    Optional<OrderedFlightInfo> findById(long id);
    List<OrderedFlightInfo> findAllByOrderId(long id);
}
