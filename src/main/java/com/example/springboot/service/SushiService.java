package com.example.springboot.service;

import java.util.Map;

import com.example.springboot.model.Result;

/**
 * Serivce for sushi order request
 * @author HUANG-QIANWEN
 */
public interface SushiService {
	/**
	 * Create order and store it in database.
	 */
	public Result createOrder(String sushi_name);
	/**
	 * Stop order if it's in progress,
	 * and label it as cancelled in database.
	 */
	public Result deleteOrderById(int order_id);
	/**
	 * Stop order if it's in progress,
	 * 
	 * and label it as paused in database.
	 */
	public Result pauseOrderById(int order_id);
	/**
	 * Stop order if it's in progress,
	 * and label it as cancelled in database.
	 */
	public Result resumeOrderById(int order_id);
	/**
	 * Get status of all orders.
	 */
	public Map<?, ?> listOrderStatus();
}
