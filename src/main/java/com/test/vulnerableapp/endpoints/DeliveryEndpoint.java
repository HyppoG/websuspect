package com.test.vulnerableapp.endpoints;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.vulnerableapp.model.DeliveryDetails;
import com.test.vulnerableapp.operations.DeliveryOperation;

@RestController
@RequestMapping("/api")
public class DeliveryEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(DeliveryEndpoint.class);

	private DeliveryOperation deliveryService;

	@Autowired
	public DeliveryEndpoint(DeliveryOperation deliveryService) {
		this.deliveryService = deliveryService;
	}

	@PostMapping("/delivery")
	public ResponseEntity<DeliveryDetails> addDelivery(@RequestBody DeliveryDetails deliveryDetails) {
		LOGGER.debug("Adding delivery details {}", deliveryDetails);

		Optional<DeliveryDetails> addedDeliveryDetails = deliveryService.addDelivery(deliveryDetails);
		if (addedDeliveryDetails.isPresent()) {
			LOGGER.debug("Delivery details added to database");
			return ResponseEntity.created(URI.create("/delivery/" + deliveryDetails.getId()))
					.body(addedDeliveryDetails.get());
		}

		return ResponseEntity.badRequest().build();
	}

}
