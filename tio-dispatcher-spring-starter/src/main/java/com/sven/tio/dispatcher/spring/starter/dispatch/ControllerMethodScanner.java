package com.sven.tio.dispatcher.spring.starter.dispatch;

import com.sven.tio.common.support.StringTool;
import com.sven.tio.dispatcher.spring.starter.annotation.TioController;
import com.sven.tio.dispatcher.spring.starter.annotation.TioMapping;
import com.sven.tio.dispatcher.spring.starter.dispatch.context.Invoker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author qinglinl
 * Created on 2022/8/29 10:19 AM
 */
@Slf4j
@Component
public class ControllerMethodScanner implements ApplicationListener<ContextRefreshedEvent> {
	private final ApplicationContext applicationContext;

	public ControllerMethodScanner(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	private void init() {
		Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(TioController.class);
		for (String s : beanMap.keySet()) {
			List<String> mappingList = new ArrayList<>();
			Object bean = beanMap.get(s);
			TioController controller = AnnotationUtils.findAnnotation(bean.getClass(), TioController.class);
			if (controller != null && StringTool.isNotBlank(controller.value())) {
				mappingList.add(controller.value());
			}

			TioMapping annotation = AnnotationUtils.findAnnotation(bean.getClass(), TioMapping.class);
			if (annotation != null && StringTool.isNotBlank(annotation.value())) {
				mappingList.add(annotation.value());
			}
			Method[] methods = bean.getClass().getMethods();
			for (Method method : methods) {
				TioMapping mappingAnnotation = AnnotationUtils.findAnnotation(method, TioMapping.class);
				if (mappingAnnotation != null) {
					String innerPath = mappingAnnotation.value();
					if (StringUtils.isNotBlank(innerPath)) {
						mappingList.add(innerPath);
					}
					String fullPath = ("/" + String.join("/", mappingList)).replaceAll("/{2,}", "/");
					Invoker invoker = new Invoker();
					invoker.setMethod(method);
					invoker.setBean(bean);
					log.info("注册TioMapping: {} -> {}#{}", fullPath, bean.getClass(), method.getName());
					TioDispatcher.addInvoker(fullPath, invoker);
				}
			}
		}
	}

	@Override
	public void onApplicationEvent(@NonNull ContextRefreshedEvent event) {
		init();
	}

}
