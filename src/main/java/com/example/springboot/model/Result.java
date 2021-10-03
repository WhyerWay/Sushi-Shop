package com.example.springboot.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

/**
 * Formatted result container includes code, message and data
 * Support JSON serialization
 * @param <T> data to be shown in response body, allowed to be null
 * @author HUANG-QIANWEN
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {
	private T order;
	private int code;
	private String msg;
	

	public T getOrder() {
		return order;
	}

	public void setOrder(T order) {
		this.order = order;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		ObjectWriter ow = new ObjectMapper().writer()
				.withDefaultPrettyPrinter();
		String json = null;
		try {
			json = ow.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return json;
	}
}
