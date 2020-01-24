package com.test.vulnerableapp.model;

import java.util.Map;

public class ShippingPrice {
	private long id;
	private long orderId;
	private String name;
	private Map<String, String> shippingType;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getShippingType() {
		return shippingType;
	}
	public void setShippingType(Map<String, String> shippingType) {
		this.shippingType = shippingType;
	}
	
	public String toString(){
		return "name "+name +" Price "+shippingType.toString();
	}
}