package com.example.springboot.model;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatTypes;

/**
 * Entity class for sushi_order table
 * Support JSON serialization and object mapping
 * @author HUANG-QIANWEN
 */
@TableName("sushi_order")
public class Order implements Serializable {
	private static final long serialVersionUID = 1L;
	@TableId(type = IdType.AUTO)
	private Integer  id;
	private Integer  statusId;
	private Integer  sushiId;
	@JsonFormat(shape = JsonFormat.Shape.NUMBER)
	private Date createdAt;
	@TableField(exist = false)
	@JsonIgnore
	private String sushiName;
	@JsonIgnore
	private Integer timeLeft;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStatusId() {
		return statusId;
	}
	public void setStatusId(Integer statusId) {
		this.statusId = statusId;
	}
	public Integer getSushiId() {
		return sushiId;
	}
	public void setSushiId(Integer sushiId) {
		this.sushiId = sushiId;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	public String getSushiName() {
		return sushiName;
	}
	public void setSushiName(String sushiName) {
		this.sushiName = sushiName;
	}
	public Integer getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(Integer timeLeft) {
		this.timeLeft = timeLeft;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "Order [id=" + id + ", statusId=" + statusId + ", sushiId=" + sushiId + ", createdAt=" + createdAt
				+ ", sushiName=" + sushiName + ", timeLeft=" + timeLeft + "]";
	}
}
