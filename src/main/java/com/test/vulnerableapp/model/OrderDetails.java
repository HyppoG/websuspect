package com.test.vulnerableapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@Entity
@Table(name="orderDetail")
public class OrderDetails implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long userId;
	private long generatedOrderId;
	
	@Transient
	private String userName;
	@Transient
	private List<OrderedFlightInfo> orderedFlights;
	@Transient
	private PaymentDetails paymentDetails;
	@Transient
	private DeliveryDetails deliveryDetails;
	@Transient
	private ShippingDetails shippingDetails;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public long getGeneratedOrderId() {
		return generatedOrderId;
	}
	public void setGeneratedOrderId(long generatedOrderId) {
		this.generatedOrderId = generatedOrderId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public List<OrderedFlightInfo> getOrderedFlights() {
		return orderedFlights;
	}
	public void setOrderedFlights(List<OrderedFlightInfo> orderedFlights) {
		this.orderedFlights = orderedFlights;
	}
	public PaymentDetails getPaymentDetails() {
		return paymentDetails;
	}
	public void setPaymentDetails(PaymentDetails paymentDetails) {
		this.paymentDetails = paymentDetails;
	}
	public DeliveryDetails getDeliveryDetails() {
		return deliveryDetails;
	}
	public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
		this.deliveryDetails = deliveryDetails;
	}
	public ShippingDetails getShippingDetails() {
		return shippingDetails;
	}
	public void setShippingDetails(ShippingDetails shippingDetails) {
		this.shippingDetails = shippingDetails;
	}
	
	public String toString(){
		return "userId "+userId+ " generatedOrderId "+generatedOrderId;
	}
	
	public boolean isValid() {
		return true;
	}
}