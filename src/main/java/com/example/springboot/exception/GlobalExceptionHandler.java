package com.example.springboot.exception;

import java.security.InvalidParameterException;

import org.h2.jdbc.JdbcException;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.springboot.model.Result;
import com.example.springboot.util.ResultUtil;

/**
 * Process exception globally and return corresponding response
 * Invalid input includes incorrect format input, value not found in database, duplicated operation
 * Unexpected error includes database operation error, database connection error, and so on.
 * @author HUANG-QIANWEN
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e) {
        if (e instanceof InvalidParameterException) {
        	return ResultUtil.error(-1, "Invalid input");
        } else {
        	e.printStackTrace();
            return ResultUtil.error(-2, "Unexpected error");
        }
    }
}
