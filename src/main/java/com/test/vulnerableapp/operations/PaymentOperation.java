package com.test.vulnerableapp.operations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vulnerableapp.model.PaymentDetails;
import com.test.vulnerableapp.model.UserInfo;
import com.test.vulnerableapp.repositories.PaymentRepository;

@Service
public class PaymentOperation {
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentOperation(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Optional<PaymentDetails> retrieveById(long id) {
        return paymentRepository.findById(id);
    }
    public Optional<PaymentDetails> retrieveByOrderId(long id) {
        return paymentRepository.findByOrderId(id);
    }

    public boolean addPayment (PaymentDetails paymentDetails) {
        if (paymentDetails.isValid()) {
            return paymentRepository.savePaymentDetails(paymentDetails);
        }
        return false;
    }
}
