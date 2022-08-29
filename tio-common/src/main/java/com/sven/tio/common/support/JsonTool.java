package com.sven.tio.common.support;

/**
 * @author qinglinl
 * Created on 2022/8/29 11:06 AM
 */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("JsonTool")
public class JsonTool implements ApplicationContextAware {
	private static ObjectMapper om = new ObjectMapper();
	static {
		init(om);
	}

	private static void init(ObjectMapper mapper) {
		if (mapper != null) {
			mapper.enable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		}
	}

	public static <T> T parse(String str, Class<T> clazz) {
		try {
			return om.readValue(str, clazz);
		} catch (JsonProcessingException e) {
			log.error("JsonTool#parse error, msg: ", e);
		}
		return null;
	}

	public static <K, V> Map<K, V> parseMap(String str, Class<K> kClass, Class<V> vClass) {
		try {
			return om.readValue(str, om.getTypeFactory().constructMapType(Map.class, kClass, vClass));
		} catch (JsonProcessingException e) {
			log.error("JsonTool#parseMap error, msg: ", e);
		}
		return new HashMap<>();
	}

	public static Map<String, Object> parseMap(String str) {
		return parseMap(str, String.class, Object.class);
	}

	public static <T> List<T> parseList(String str, Class<T> clazz) {
		try {
			return om.readValue(str, om.getTypeFactory().constructCollectionType(List.class, clazz));
		} catch (JsonProcessingException e) {
			log.error("JsonTool#parseList error, msg: ", e);
		}
		return new ArrayList<>();
	}

	public static <T> T convert2Value(Object value, Class<T> clazz) {
		if (value == null) {
			return null;
		}
		try {
			return om.convertValue(value, clazz);
		} catch (IllegalArgumentException e) {
			log.error("JsonTool#convert2Value error, msg: ", e);
		}
		return null;
	}

	public static <T> List<T> convert2List(Object value, Class<T> clazz) {
		try {
			return om.convertValue(value, om.getTypeFactory().constructCollectionType(List.class, clazz));
		} catch (IllegalArgumentException e) {
			log.error("JsonTool#convert2List error, msg: ", e);
		}
		return new ArrayList<>();
	}

	public static String toJSONString(Object o) {
		try {
			return om.writeValueAsString(o);
		} catch (JsonProcessingException e) {
			log.error("JsonTool#toJSONString error, msg: ", e);
		}
		return null;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		JsonTool.om = applicationContext.getBean(ObjectMapper.class);
		init(JsonTool.om);
	}
}
