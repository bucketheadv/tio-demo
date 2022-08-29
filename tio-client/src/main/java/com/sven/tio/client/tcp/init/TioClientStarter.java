package com.sven.tio.client.tcp.init;

import com.sven.tio.common.model.MessageDTO;
import com.sven.tio.common.model.UserInfoDTO;
import com.sven.tio.common.packet.MessagePacket;
import com.sven.tio.common.support.JsonTool;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tio.client.ClientChannelContext;
import org.tio.client.TioClient;
import org.tio.core.Node;
import org.tio.core.Tio;

import javax.annotation.Resource;

/**
 * @author qinglinl
 * Created on 2022/8/24 1:47 PM
 */
@Component
public class TioClientStarter implements CommandLineRunner {
	@Resource
	private TioClient tioClient;
	@Resource
	private Node serverNode;

	public static void send(ClientChannelContext clientChannelContext) throws Exception {
		MessagePacket packet = new MessagePacket();
		UserInfoDTO userInfoDTO = new UserInfoDTO();
		userInfoDTO.setUserId(1L);
		userInfoDTO.setName("User1");
		MessageDTO messageDTO = new MessageDTO();
		messageDTO.setPath("/user/login");
		messageDTO.setData(JsonTool.toJSONString(userInfoDTO));
		String messageBody = JsonTool.toJSONString(messageDTO);
		if (messageBody != null) {
			packet.setBody(messageBody.getBytes(MessagePacket.CHARSET));
		}
		Tio.send(clientChannelContext, packet);
	}
	@Override
	public void run(String... args) throws Exception {
		ClientChannelContext clientChannelContext = tioClient.connect(serverNode);
		send(clientChannelContext);
	}
}
