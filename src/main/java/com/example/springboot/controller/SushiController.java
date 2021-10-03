package com.example.springboot.controller;

import java.security.InvalidParameterException;
import java.util.Map;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.springboot.model.Result;
import com.example.springboot.service.SushiService;

/**
 * Process REST API request and return result of service in JSON format
 * @author HUANG-QIANWEN
 */
@RestController
@MapperScan("com.example.springboot.mapper")
public class SushiController {
	@Autowired 
	SushiService sushiService;
	
	@PostMapping("/api/orders")
	public ResponseEntity<Result> submitAnOrder(@RequestParam String sushi_name) throws Exception {
		if(StringUtils.isBlank(sushi_name)) {
			throw new InvalidParameterException();
		}
		Result result = sushiService.createOrder(sushi_name);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/api/orders/{order_id}")
	public Result deleteOrder(@PathVariable("order_id") Integer id) {
		return sushiService.deleteOrderById(id);
	}
	
	@PutMapping("/api/orders/{order_id}/pause")
	public Result pauseOrder(@PathVariable("order_id") Integer id) {
		return sushiService.pauseOrderById(id);
	}
	
	@PutMapping("/api/orders/{order_id}/resume")
	public Result resumeOrder(@PathVariable("order_id") Integer id) {
		return sushiService.resumeOrderById(id);
	}

	@GetMapping("/api/orders/status")
	public Map<?, ?> listOrder() {
		return sushiService.listOrderStatus();
	}
}