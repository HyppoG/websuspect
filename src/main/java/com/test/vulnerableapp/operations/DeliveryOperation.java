package com.test.vulnerableapp.operations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vulnerableapp.model.DeliveryDetails;
import com.test.vulnerableapp.repositories.DeliveryRepository;

@Service
public class DeliveryOperation {
    private DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryOperation(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public Optional<DeliveryDetails> retrieveById(long id) {
        return deliveryRepository.findById(id);
    }
    public Optional<DeliveryDetails> retrieveByOrderId(long id) {
        return deliveryRepository.findByOrderId(id);
    }

    public Optional<DeliveryDetails> addDelivery(DeliveryDetails deliveryDetails) {
        /*if (deliveryDetails.isValid()) {

        }*/

        return Optional.of(deliveryRepository.saveAndFlush(deliveryDetails));
        //return Optional.empty();
    }

    public Optional<DeliveryDetails> addDeliveryDetails(DeliveryDetails deliveryDetails) {
        /*if (feedback.isValid()) {
            return Optional.of(feedbackRepository.saveAndFlush(feedback));
        }*/

        return Optional.of(deliveryRepository.saveAndFlush(deliveryDetails));
        //return Optional.empty();
    }
}
