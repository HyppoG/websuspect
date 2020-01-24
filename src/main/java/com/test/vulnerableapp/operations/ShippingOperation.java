package com.test.vulnerableapp.operations;

import com.test.vulnerableapp.model.ShippingDetails;
import com.test.vulnerableapp.repositories.ShippingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ShippingOperation {

    private ShippingRepository shippingRepository;

    @Autowired
    public ShippingOperation(ShippingRepository shippingRepository) {
        this.shippingRepository = shippingRepository;
    }

    public Optional<ShippingDetails> retrieveById(long id) {
        return shippingRepository.findById(id);
    }
    public Optional<ShippingDetails> retrieveByOrderId(long id) {
        return shippingRepository.findByOrderId(id);
    }

    public Optional<ShippingDetails> addShippingDetails(ShippingDetails shippingDetails) {
        /*if (feedback.isValid()) {
            return Optional.of(feedbackRepository.saveAndFlush(feedback));
        }*/

        return Optional.of(shippingRepository.saveAndFlush(shippingDetails));
        //return Optional.empty();
    }
}
