package com.test.vulnerableapp.operations;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vulnerableapp.model.Flight;
import com.test.vulnerableapp.repositories.FlightRepository;

@Service
public class FlightOperation {
    private FlightRepository flightRepository;

    @Autowired
    public FlightOperation(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public Optional<Flight> retrieveById(long id) {
        return flightRepository.findById(id);
    }
    
    public List<Flight> retrieveAll() {
        return flightRepository.findAll();
    }
}
