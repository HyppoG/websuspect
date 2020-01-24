package com.test.vulnerableapp.endpoints;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.operations.OrderOperation;

@RestController
@RequestMapping("/api")
public class OrderEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrderEndpoint.class);

	private OrderOperation orderService;

	@Autowired
	public OrderEndpoint(OrderOperation orderService) {
		this.orderService = orderService;
	}

	@GetMapping("/order/{id}")
	public ResponseEntity<OrderDetails> getOrder(@PathVariable long id) {
		LOGGER.debug("Getting order details with id {}", id);

		try {
			return ResponseEntity.ok(orderService.retrieveById(id).orElseThrow(RuntimeException::new));
		} catch (RuntimeException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}

	@PostMapping("/order")
	public ResponseEntity addOrder(@RequestBody OrderDetails orderDetails) {
		LOGGER.debug("Adding order details {}", orderDetails);

		Optional<OrderDetails> addedOrder = orderService.addOrderDetails(orderDetails);
		if (addedOrder.isPresent()) {
			LOGGER.debug("Order details added to database");
			return ResponseEntity.created(URI.create("/order/" + orderDetails.getId())).body(addedOrder.get());
		}

		return ResponseEntity.badRequest().build();
	}

}
