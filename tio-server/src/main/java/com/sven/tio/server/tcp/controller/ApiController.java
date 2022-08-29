package com.sven.tio.server.tcp.controller;

import com.sven.tio.common.model.UserInfoDTO;
import com.sven.tio.dispatcher.spring.starter.annotation.TioController;
import com.sven.tio.dispatcher.spring.starter.annotation.TioMapping;
import lombok.extern.slf4j.Slf4j;

/**
 * @author qinglinl
 * Created on 2022/8/26 2:30 PM
 */
@Slf4j
@TioController("api")
@TioMapping("/base")
public class ApiController {
	@TioMapping("/hello")
	public Object hello(UserInfoDTO userInfoDTO) {
		log.info("Hello + " + userInfoDTO.getName());
		return userInfoDTO;
	}
}
