package com.sven.tio.dispatcher.spring.starter.dispatch;

import com.sven.tio.common.support.JsonTool;
import com.sven.tio.dispatcher.spring.starter.dispatch.context.Invoker;
import com.sven.tio.dispatcher.spring.starter.exception.InvokerNotFoundException;
import com.sven.tio.dispatcher.spring.starter.dispatch.context.DispatcherContext;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author qinglinl
 * Created on 2022/8/26 2:04 PM
 */
@Component
public class TioDispatcher {
	private static final Map<String, Invoker> map = new ConcurrentHashMap<>();

	private Invoker getInvoker(String path) {
		return map.get(path);
	}

	@SuppressWarnings("unchecked")
	public <T> T invoke(String path, DispatcherContext context) throws InvokerNotFoundException, IllegalAccessException, InvocationTargetException {
		Invoker invoker = getInvoker(path);
		if (invoker == null) {
			throw new InvokerNotFoundException();
		}
		Method method = invoker.getMethod();
		Object data = context.getData();
		Object[] args = new Object[method.getParameterCount()];
		for (int i = 0; i < method.getParameterCount(); i++) {
			Class<?> c = method.getParameterTypes()[i];
			if (c == ChannelContext.class) {
				args[i] = context.getChannelContext();
			} else {
				if (data instanceof String) {
					args[i] = JsonTool.parse(String.valueOf(data), c);
				} else {
					args[i] = JsonTool.convert2Value(data, c);
				}
			}
		}
		return (T) method.invoke(invoker.getBean(), args);
	}

	static void addInvoker(String path, Invoker invoker) {
		if (map.get(path) != null) {
			throw new RuntimeException("TioMapping重复注册, 地址: " + path);
		}
		map.put(path, invoker);
	}
}
