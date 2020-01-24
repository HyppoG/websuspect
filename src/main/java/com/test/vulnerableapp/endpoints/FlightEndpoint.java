package com.test.vulnerableapp.endpoints;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.vulnerableapp.model.Flight;
import com.test.vulnerableapp.operations.FlightOperation;

@RestController
@RequestMapping("/api")
public class FlightEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(FlightEndpoint.class);

	private FlightOperation flightService;

	@Autowired
	public FlightEndpoint(FlightOperation flightService) {
		this.flightService = flightService;
	}

	@GetMapping("/flight/{id}")
	public ResponseEntity<Flight> getFlight(@PathVariable long id) {
		LOGGER.debug("Getting delivery details with id {}", id);

		try {
			return ResponseEntity.ok(flightService.retrieveById(id).orElseThrow(RuntimeException::new));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
	
	@GetMapping("/flight/")
	public ResponseEntity<List<Flight>> getFlights() {
		LOGGER.debug("Getting all flights");

		return ResponseEntity.ok(flightService.retrieveAll());
	}


}
