package com.test.vulnerableapp.operations;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.repositories.OrderRepository;

@Service
public class OrderOperation {
    private OrderRepository orderRepository;

    @Autowired
    public OrderOperation(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Optional<OrderDetails> retrieveById(long id) {
        return orderRepository.findById(id);
    }

    public Boolean existsById(long id){
        return orderRepository.existsById(id);
    }

    public Optional<OrderDetails> addOrderDetails(OrderDetails orderDetails) {
        if (orderDetails.isValid()) {
            return Optional.of(orderRepository.saveAndFlush(orderDetails));
        }

        return Optional.empty();
    }

}
