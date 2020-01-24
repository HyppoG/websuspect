package com.test.vulnerableapp.operations;

import com.test.vulnerableapp.model.OrderedFlightInfo;
import com.test.vulnerableapp.repositories.OrderedFlightInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderedFlightInfoOperation {
    private OrderedFlightInfoRepository orderedFlightInfoRepository;

    @Autowired
    public OrderedFlightInfoOperation(OrderedFlightInfoRepository orderedFlightInfoRepository) {
        this.orderedFlightInfoRepository = orderedFlightInfoRepository;
    }

    public Optional<OrderedFlightInfo> retrieveById(long id) {
        return orderedFlightInfoRepository.findById(id);
    }

    public Boolean existsById(long id){
        return orderedFlightInfoRepository.existsById(id);
    }

    public Optional<OrderedFlightInfo> addOrderedFlightInfo(OrderedFlightInfo orderedFlightInfo) {
        /*if (orderedFlightInfo.isValid()) {

        }

        return Optional.empty();*/
        // TODO : Fix me
        return Optional.of(orderedFlightInfoRepository.saveAndFlush(orderedFlightInfo));
    }

    public void addListOrderedFlights(List<OrderedFlightInfo> listOrderedFlights){
        for (OrderedFlightInfo orderedFlightInfo : listOrderedFlights) {
            orderedFlightInfoRepository.saveAndFlush(orderedFlightInfo);
        }
    }

    public List<OrderedFlightInfo> retrieveAllByOrderId(long id) { return orderedFlightInfoRepository.findAllByOrderId(id); }

}
