package com.example.springboot.model;

/**
 * Entity class for result set of listing order status
 * Support JSON serialization and object mapping
 * @author HUANG-QIANWEN
 */
public class OrderStatus {
	private int orderId;
	private int timeSpent;
	public int getOrderId() {
		return orderId;
	}
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}
	public int getTimeSpent() {
		return timeSpent;
	}
	public void setTimeSpent(int timeSpent) {
		this.timeSpent = timeSpent;
	}
	@Override
	public String toString() {
		return "OrderStatus [orderId=" + orderId + ", timeSpent=" + timeSpent + "]";
	}
}
