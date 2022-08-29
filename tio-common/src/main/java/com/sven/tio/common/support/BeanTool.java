package com.sven.tio.common.support;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author qinglinl
 * Created on 2022/8/29 11:07 AM
 */
@Slf4j
public class BeanTool {
	private BeanTool() {}

	public static <T> T copyAs(Object source, Class<T> clazz) {
		if (source == null) {
			return null;
		}
		try {
			T t = clazz.getConstructor().newInstance();
			BeanUtils.copyProperties(source, t);
			return t;
		} catch (Exception e) {
			log.error("BeanTool#copyAs异常: ", e);
			return null;
		}
	}

	public static <T> List<T> copyList(Collection<?> c, Class<T> clazz) {
		List<T> result = new ArrayList<>();
		if (CollectionTool.isEmpty(c)) {
			return result;
		}
		for (Object o : c) {
			result.add(copyAs(o, clazz));
		}
		return result;
	}
}
