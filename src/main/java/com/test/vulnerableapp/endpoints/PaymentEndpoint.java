package com.test.vulnerableapp.endpoints;

import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.test.vulnerableapp.model.PaymentDetails;
import com.test.vulnerableapp.operations.PaymentOperation;

@RestController
@RequestMapping("/api")
public class PaymentEndpoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(PaymentEndpoint.class);

    private PaymentOperation paymentService;

    @Autowired
    public PaymentEndpoint(PaymentOperation paymentService) {
        this.paymentService = paymentService;
    }
    
    @GetMapping("/payment/{id}")
    public ResponseEntity<PaymentDetails> getPayment(@PathVariable long id) {
        LOGGER.debug("Getting payment details with id {}", id);

        try {
            return ResponseEntity.ok(paymentService.retrieveById(id).orElseThrow(RuntimeException::new));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @PostMapping("/payment")
    public ResponseEntity addPayment(@RequestBody PaymentDetails paymentDetails) {
        LOGGER.debug("Adding payment details {}", paymentDetails);

        //Optional<PaymentDetails> addedPaymentDetails = paymentService.addPayment(paymentDetails);
        //if (addedPaymentDetails.isPresent()) {
        // Fixme
        boolean addedPaymentDetails = paymentService.addPayment(paymentDetails);
        if (addedPaymentDetails){
            LOGGER.debug("Payment details added to database");
            return ResponseEntity
                    .created(URI.create("/payment/" + paymentDetails.getId()))
                    .body(paymentDetails);
        }

        return ResponseEntity.badRequest().build();
    }
    
}
