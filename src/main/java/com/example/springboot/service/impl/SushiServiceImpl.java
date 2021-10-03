package com.example.springboot.service.impl;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.example.springboot.mapper.OrderMapper;
import com.example.springboot.model.Order;
import com.example.springboot.model.OrderStatus;
import com.example.springboot.model.Result;
import com.example.springboot.model.Status;
import com.example.springboot.service.SushiService;
import com.example.springboot.thread.Chef;
import com.example.springboot.util.ResultUtil;

@Service
public class SushiServiceImpl implements SushiService {

	@Autowired
	OrderMapper orderMapper;
	
	@Autowired
	ApplicationContext applicationContext;
	
	private List<Chef> chefList = new ArrayList<>();
	
	@Override
	public Result createOrder(String sushi_name) {
		if(chefList.size() < Chef.CHEF_NUMBER) {
			chefInit();
		}
		Order order = new Order();
		order.setSushiName(sushi_name);
		if(orderMapper.countSushiName(sushi_name) != 1) {
			throw new InvalidParameterException();
		}
		if(orderMapper.createOrder(order) == 1) {
			order = orderMapper.selectById(order.getId());
			return ResultUtil.success(order, "Order created");
		}else {
			return ResultUtil.error(-1, "Order created fail");
		}
	}

	@Override
	public Result deleteOrderById(int order_id) {
		String status = orderMapper.getStatusById(order_id);
		switch (status) {
		case Status.CREATED:
			if(orderMapper.setOrderCancel(order_id) != 1) {
				return ResultUtil.error(-1, "Cancel fail");
			}
			break;
		case Status.PAUSED:
			if(orderMapper.setOrderCancel(order_id) != 1) {
				return ResultUtil.error(-1, "Cancel fail");
			}
			break;
		case Status.In_PROGRESS:
			for(Chef chef : chefList) {
				if(order_id == chef.getCurrentOrderId()) {
					if(!chef.cancelOrder()) {
						return ResultUtil.error(-1, "Cancel fail");
					}
					break;
				}
			}
			break;
		default:
			throw new InvalidParameterException();
		}
		return ResultUtil.success(null, "Order cancelled");
	}

	@Override
	public Result pauseOrderById(int order_id) {
		String status = orderMapper.getStatusById(order_id);
		switch (status) {
		case Status.CREATED:
			if(orderMapper.setOrderPause(order_id) != 1) {
				return ResultUtil.error(-1, "Pause fail");
			}
			break;
		case Status.In_PROGRESS:
			for(Chef chef : chefList) {
				if(order_id == chef.getCurrentOrderId()) {
					if(!chef.pauseOrder()) {
						return ResultUtil.error(-1, "Pause fail");
					}
					break;
				}
			}
			break;
		default:
			throw new InvalidParameterException();
		}
		return ResultUtil.success(null, "Order paused");
	}

	@Override
	public Result resumeOrderById(int order_id) {
		String status = orderMapper.getStatusById(order_id);
		if(!Status.PAUSED.equals(status)) {
			throw new InvalidParameterException();
		}
		Order order = orderMapper.selectById(order_id);
		Chef.resume(order);
		return ResultUtil.success(null, "Order resumed");
	}

	@Override
	public Map<?, ?> listOrderStatus() {
		Map<String, OrderStatus[]> res = new HashMap<String, OrderStatus[]>();
		OrderStatus[] inProgress = orderMapper.listOrderInProgress();
		updateInProgress(inProgress);
		res.put(Status.In_PROGRESS, inProgress);
		res.put(Status.CREATED, orderMapper.listOrderByStatus(Status.CREATED));
		res.put(Status.PAUSED, orderMapper.listOrderByStatus(Status.PAUSED));
		res.put(Status.CANCELLED, orderMapper.listOrderByStatus(Status.CANCELLED));
		res.put("completed", orderMapper.listOrderByStatus(Status.FINISHED));
		return ResultUtil.success(res);
	}

	private void chefInit() {
		for(int i = 0; i < Chef.CHEF_NUMBER; i++) {
			Chef chef = applicationContext.getBean(Chef.class);
			chef.setName("Chef " + i);
			chefList.add(chef);
			chef.start();
		}
	}
	
	private void updateInProgress(OrderStatus[] inProgress) {
		Map<Integer, Integer> map = new HashMap<>();
		for(Chef chef : chefList) {
			if(chef.getCurrentOrderId() != -1) {
				map.put(chef.getCurrentOrderId(), chef.getOrderTimeLeft());
			}
		}
		for(OrderStatus status : inProgress) {
			status.setTimeSpent(status.getTimeSpent() - map.get(status.getOrderId()));
		}
	}
}
