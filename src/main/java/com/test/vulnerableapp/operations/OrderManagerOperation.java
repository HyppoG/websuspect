package com.test.vulnerableapp.operations;

import com.test.vulnerableapp.model.OrderDetails;
import com.test.vulnerableapp.model.OrderedFlightInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderManagerOperation {
	private static final Logger logger = LoggerFactory.getLogger(OrderManagerOperation.class);
	@Autowired
	private OrderOperation orderOperation;
	@Autowired
	private OrderedFlightInfoOperation orderedFlightInfoOperation;
	@Autowired
	private PaymentOperation paymentOperation;
	@Autowired
	private DeliveryOperation deliveryOperation;
	@Autowired
	private ShippingOperation shippingOperation;

	public boolean saveOrderDetailsInDb(OrderDetails orderDetails) {
		// TODO Fix get()
		long id = orderOperation.addOrderDetails(orderDetails).get().getId();
		if (id < 1)
			return false;
		orderDetails.getDeliveryDetails().setOrderId(id);
		orderDetails.getPaymentDetails().setOrderId(id);
		orderDetails.getShippingDetails().setOrderId(id);
		for (OrderedFlightInfo orderedFlightInfo : orderDetails.getOrderedFlights()) {
			orderedFlightInfo.setOrderId(id);
		}
		paymentOperation.addPayment(orderDetails.getPaymentDetails());
		deliveryOperation.addDeliveryDetails(orderDetails.getDeliveryDetails());
		shippingOperation.addShippingDetails(orderDetails.getShippingDetails());
		orderedFlightInfoOperation.addListOrderedFlights(orderDetails.getOrderedFlights());
		return true;
	}

	public OrderDetails getOrderDetails(long generatedOrderId) {
		OrderDetails orderDetails = new OrderDetails();
		// TODO : Verify get()
		orderDetails = orderOperation.retrieveById(generatedOrderId).get();
		if (orderDetails == null || orderDetails.getId() < 1)
			return null;
		long orderId = orderDetails.getId();
		// TODO Not .get() - Check not empty before setting details
		orderDetails.setPaymentDetails(paymentOperation.retrieveByOrderId(orderId).get());
		orderDetails.setDeliveryDetails(deliveryOperation.retrieveByOrderId(orderId).get());
		orderDetails.setShippingDetails(shippingOperation.retrieveByOrderId(orderId).get());
		orderDetails.setOrderedFlights(orderedFlightInfoOperation.retrieveAllByOrderId(orderId));
		return orderDetails;
	}
}