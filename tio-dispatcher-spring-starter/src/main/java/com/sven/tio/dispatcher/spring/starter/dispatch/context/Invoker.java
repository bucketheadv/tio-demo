package com.sven.tio.dispatcher.spring.starter.dispatch.context;

import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author qinglinl
 * Created on 2022/8/29 10:37 AM
 */
@Data
public class Invoker {
	private Object bean;

	private Method method;
}
