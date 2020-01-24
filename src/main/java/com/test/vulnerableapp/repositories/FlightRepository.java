package com.test.vulnerableapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.test.vulnerableapp.model.Flight;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findById(long id);
}
