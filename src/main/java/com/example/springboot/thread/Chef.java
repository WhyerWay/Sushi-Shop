package com.example.springboot.thread;

import java.util.concurrent.ConcurrentLinkedQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.example.springboot.mapper.OrderMapper;
import com.example.springboot.model.Order;
import com.example.springboot.model.Status;

/**
 * Simulate chef to process order or find available order to process
 * @author HUANG-QIANWEN
 */
@Component
@Scope("prototype")
public class Chef extends Thread{
	/**
	 * Allowed amount of chef
	 */
	public static final int CHEF_NUMBER = 3;
	/**
	 * Store resumed order, support FIFO
	 */
	private static ConcurrentLinkedQueue<Order> resumed_order = new ConcurrentLinkedQueue<>();
	/**
	 * Operate suchi_order table
	 */
	@Autowired
	private OrderMapper orderMapper;
	/**
	 * Running flag of chef
	 */
	private boolean running = true;
	/**
	 * Current processed order of chef
	 */
	private Order currentOrder = null;
	/**
	 * Estimated finish time of current order
	 */
	private Long orderFinishTime = 0L;
	/**
	 * Constantly process order or find available order to process
	 */
	@Override
	public void run() {
		while(running) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			if(currentOrder == null) { // Looking for order
				try {
					if(!resumed_order.isEmpty()) { // Pick resumed order first
						currentOrder = resumed_order.poll();
					}else { // Pick created order
						currentOrder = orderMapper.getNextOrder(Status.CREATED);
					}
					orderFinishTime = System.currentTimeMillis() + 1000 * currentOrder.getTimeLeft();
					if(orderMapper.setOrderInProgress(currentOrder) == 1) {
						System.out.println(this.getName() + " : Order " + currentOrder.getId() + " starting...");
					}else {
						currentOrder = null;
					}
				} catch (Exception e) {
					currentOrder = null;
				}
			}else { // Processing order
				if(System.currentTimeMillis() > orderFinishTime) { // Order finished
					Order rollback = currentOrder;
					try {
						if(orderMapper.setOrderComplete(currentOrder) == 1) {
							System.out.println(this.getName() + " : Order " + currentOrder.getId() + " finished!");
							currentOrder = null;
						}else {
							currentOrder = rollback;
						}
					} catch (Exception e) {
						currentOrder = rollback;
					}
				}else { // Update time to count time spent
					currentOrder.setTimeLeft((int) ((orderFinishTime - System.currentTimeMillis()) / 1000));
				}
			}
		}
	}
	
	/**
	 * Cancel current order and update database
	 * @return true if success, false if fail
	 * @author Qianwen Huang
	 */
	public boolean cancelOrder() {
		try {
			long time_left =  (orderFinishTime - System.currentTimeMillis()) / 1000;
			currentOrder.setTimeLeft((int) time_left);
			if(orderMapper.setOrderCancelWithTime(currentOrder) != 1) {
				return false;
			}
			System.out.println(this.getName() + " : Order " + currentOrder.getId() + " Cancelled!");
			currentOrder = null;
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	/**
	 * Pause current order and update database
	 * @return true if success, false if fail
	 * @author Qianwen Huang
	 */
	public boolean pauseOrder() {
		try {
			long time_left =  (orderFinishTime - System.currentTimeMillis()) / 1000;
			currentOrder.setTimeLeft((int) time_left);
			if(orderMapper.setOrderPauseWithTime(currentOrder) != 1) {
				return false;
			}
			System.out.println(this.getName() + " : Order " + currentOrder.getId() + " Paused!");
			currentOrder = null;
			return true;
		} catch (Exception e) {
			throw e;
		}
	}
	
	public void activate() {
		running = true;
	}
	
	public void deactivate() {
		running = false;
	}
	/**
	 * Get order id of current order
	 * @return -1 if no order on hand, otherwise return id of current order
	 * @author Qianwen Huang
	 */
	public int getCurrentOrderId() {
		if(currentOrder == null) {
			return -1;
		}
		return currentOrder.getId();
	}
	/**
	 * Get time left in second of current order
	 * @return -1 if no order on hand, otherwise return time left of current order
	 * @author Qianwen Huang
	 */
	public int getOrderTimeLeft() {
		if(currentOrder == null) {
			return -1;
		}
		return currentOrder.getTimeLeft();
	}
	/**
	 * Place order into resumed queue
	 * @param order Order to be resumed
	 * @author Qianwen Huang
	 */
	public static void resume(Order order) {
		resumed_order.offer(order);
	}
}
