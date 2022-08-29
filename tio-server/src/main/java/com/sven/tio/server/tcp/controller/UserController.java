package com.sven.tio.server.tcp.controller;

import com.sven.tio.common.model.UserInfoDTO;
import com.sven.tio.common.packet.MessagePacket;
import com.sven.tio.dispatcher.spring.starter.annotation.TioController;
import com.sven.tio.dispatcher.spring.starter.annotation.TioMapping;
import lombok.extern.slf4j.Slf4j;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;

/**
 * @author qinglinl
 * Created on 2022/8/29 1:41 PM
 */
@Slf4j
@TioController
@TioMapping("/user")
public class UserController {
	@Resource
	private TioConfig tioConfig;

	@TioMapping("/login")
	public Object login(UserInfoDTO userInfoDTO, ChannelContext channelContext) {
		Tio.bindUser(channelContext, userInfoDTO.getUserId().toString());
		log.info("绑定用户会话成功: {}", userInfoDTO.getUserId());
		MessagePacket messagePacket = new MessagePacket();
		messagePacket.setBody("Hello".getBytes(StandardCharsets.UTF_8));
		Tio.sendToUser(tioConfig, userInfoDTO.getUserId().toString(), messagePacket);
		return userInfoDTO;
	}
}
