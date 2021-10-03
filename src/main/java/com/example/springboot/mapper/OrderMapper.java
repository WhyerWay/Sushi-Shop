package com.example.springboot.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Configurable;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.model.Order;
import com.example.springboot.model.OrderStatus;

/**
 * Support basic CRUD and specific operations on suchi_order table
 * @author HUANG-QIANWEN
 */
@Mapper
public interface OrderMapper extends BaseMapper<Order> {
	
	/******************************************************************************************
	 *                                    Insert
	 ******************************************************************************************/                                    
	/* 
	 * Insert order with sushi name, status is created by default
	 * return auto generated id
	 */
	
	@Insert("insert into sushi_order (status_id, sushi_id, time_left) "
			+ "values (select id from status where name = 'created'"
			+ ",select id from sushi where name = #{sushiName}"
			+ ",select time_to_make from sushi where name = #{sushiName})")
	@Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
	public Integer createOrder(Order order);
	
	/******************************************************************************************
	 *                                    Select
	 ******************************************************************************************/
	
	/**
	 * Validate sushi name
	 * @return 1 if exists, 0 if not
	 */
	@Select("select count(*) from sushi where name = #{sushiName}")
	public int countSushiName(String sushiName);
	
	/**
	 * Check staus of order by id
	 * @return Status name
	 */
	@Select("select name from status s where s.id = "
			+ "(select status_id from sushi_order o where o.id = #{id})")
	public String getStatusById(Integer id);
	
	/**
	 * List time left of orders by given status
	 */
	@Select("select s.id as order_id, "
			+ "sushi.time_to_make - s.time_left as time_spent "
			+ "from sushi_order s "
			+ "left join sushi on s.sushi_id = sushi.id "
			+ "left join status on s.status_id = status.id "
			+ "where status.name = #{status_name}")
	public OrderStatus[] listOrderByStatus(String status_name);
	
	/**
	 * List total time of in-progress orders
	 */
	@Select("select s.id as order_id, "
			+ "sushi.time_to_make as time_spent "
			+ "from sushi_order s "
			+ "left join sushi on s.sushi_id = sushi.id "
			+ "left join status on s.status_id = status.id "
			+ "where status.name = 'in-progress'")
	public OrderStatus[] listOrderInProgress();
	
	@Select("select * from sushi_order where "
			+ "status_id = (select id from status where name = #{status}) "
			+ "order by id limit 1")
	public Order getNextOrder(String status);
	
	/*******************************************************************************************
	 *                                   Update
	 *******************************************************************************************/                                   
	/*
	 * Update status of order to (in-progress, finished, paused, cancelled)
	 * with/without updating time left of the order
	 */
	
	@Update("update sushi_order set status_id = (select id from status where name = 'in-progress') "
			+ "where id = #{id}"
			+ "and status_id != (select id from status where name = 'in-progress')")
	public int setOrderInProgress(Order order);
	
	@Update("update sushi_order set (status_id, time_left) = "
			+ "(select id from status where name = 'finished', 0) "
			+ "where id = #{id} "
			+ "and status_id != (select id from status where name = 'finished')")
	public int setOrderComplete(Order order);
	
	@Update("update sushi_order set (status_id, time_left) = "
			+ "(select id from status where name = 'paused', #{timeLeft}) "
			+ "where id = #{id} "
			+ "and status_id != (select id from status where name = 'paused')")
	public int setOrderPauseWithTime(Order order);
	
	@Update("update sushi_order set status_id = "
			+ "(select id from status where name = 'paused') "
			+ "where id = #{id} "
			+ "and status_id != (select id from status where name = 'paused')")
	public int setOrderPause(Integer id);
	
	@Update("update sushi_order set (status_id, time_left) = "
			+ "(select id from status where name = 'cancelled', #{timeLeft}) "
			+ "where id = #{id} "
			+ "and status_id != (select id from status where name = 'cancelled')")
	public int setOrderCancelWithTime(Order order);
	
	@Update("update sushi_order set status_id = "
			+ "(select id from status where name = 'cancelled') "
			+ "where id = #{id} "
			+ "and status_id != (select id from status where name = 'cancelled')")
	public int setOrderCancel(Integer id);
}
