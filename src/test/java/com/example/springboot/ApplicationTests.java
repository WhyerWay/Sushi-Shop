package com.example.springboot;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.springboot.mapper.OrderMapper;
import com.example.springboot.model.Order;
import com.example.springboot.model.OrderStatus;
import com.example.springboot.model.Status;

/**
 * Validate database operations
 * @author HUANG-QIANWEN
 */
@SpringBootTest
@RunWith(SpringRunner.class)
class ApplicationTests {
	@Autowired
	OrderMapper orderMapper;
	
	@Test
	public void dbTest() {
		Order order1 = new Order();
		order1.setSushiName("California Roll");
		orderMapper.createOrder(order1);
		Assertions.assertEquals(1, order1.getId().intValue(), "Insert fail");
		
		List<Order> orderList = orderMapper.selectList(null);
		Assertions.assertEquals(1, orderList.size(), "Insert fail");
		orderList.forEach(System.out::println);

		Order order2 = orderMapper.getNextOrder(Status.CREATED);
		Assertions.assertEquals(1, order2.getId().intValue(), "Pick order fail");
		
		int n1 = orderMapper.setOrderCancel(order1.getId());
		Assertions.assertEquals(1, n1, "Update fail");
		int n2 = orderMapper.setOrderCancel(order1.getId());
		Assertions.assertEquals(0, n2, "Update fail");
		OrderStatus[] status = orderMapper.listOrderByStatus(Status.CANCELLED);
		Assertions.assertEquals(1, status.length, "Update fail");
  }
}
