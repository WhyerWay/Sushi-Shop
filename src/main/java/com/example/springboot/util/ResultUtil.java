package com.example.springboot.util;

import java.util.Map;

import com.example.springboot.model.Order;
import com.example.springboot.model.Result;

/**
 * Package data as formatted result
 * @author HUANG-QIANWEN
 */
public class ResultUtil {
    public static Map<?, ?> success(Map<?, ?> res) {
        return res;
    }
    
    public static Result<Object> success(Object order, String msg) {
        Result<Object> result = new Result<>();
        result.setCode(0);
        result.setMsg(msg);
        result.setOrder(order);
        return result;
    }
    
    public static Result error(int code, String msg) {
        Result<Order> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}
