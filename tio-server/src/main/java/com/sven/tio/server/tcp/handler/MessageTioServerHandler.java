package com.sven.tio.server.tcp.handler;

import com.sven.tio.dispatcher.spring.starter.dispatch.context.DispatcherContext;
import com.sven.tio.common.model.MessageDTO;
import com.sven.tio.common.packet.MessagePacket;
import com.sven.tio.common.support.JsonTool;
import com.sven.tio.dispatcher.spring.starter.dispatch.TioDispatcher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.TioConfig;
import org.tio.core.exception.TioDecodeException;
import org.tio.core.intf.Packet;
import org.tio.server.intf.TioServerHandler;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @author qinglinl
 * Created on 2022/8/24 1:28 PM
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MessageTioServerHandler implements TioServerHandler {
	private final TioDispatcher tioDispatcher;

	@Override
	public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws TioDecodeException {
		if (readableLength < MessagePacket.HEADER_LENGTH) {
			return null;
		}

		int bodyLength = buffer.getInt();
		if (bodyLength < 0) {
			throw new TioDecodeException("bodyLength[" + bodyLength + "] is not right, remote: " + channelContext.getClientNode());
		}

		int neededLength = MessagePacket.HEADER_LENGTH + bodyLength;

		int isDataEnough = readableLength - neededLength;
		if (isDataEnough < 0) {
			return null;
		}
		MessagePacket imPacket = new MessagePacket();
		if (bodyLength > 0) {
			byte[] dst = new byte[bodyLength];
			buffer.get(dst);
			imPacket.setBody(dst);
		}

		return imPacket;
	}

	@Override
	public ByteBuffer encode(Packet packet, TioConfig tioConfig, ChannelContext channelContext) {
		MessagePacket imPacket = (MessagePacket) packet;
		byte[] body = imPacket.getBody();
		int bodyLen = 0;
		if (body != null) {
			bodyLen = body.length;
		}

		int allLen = MessagePacket.HEADER_LENGTH + bodyLen;
		ByteBuffer buffer = ByteBuffer.allocate(allLen);
		buffer.order(tioConfig.getByteOrder());
		buffer.putInt(bodyLen);
		if (body != null) {
			buffer.put(body);
		}
		return buffer;
	}

	@Override
	public void handler(Packet packet, ChannelContext channelContext) throws Exception {
		MessagePacket imPacket = (MessagePacket) packet;
		byte[] body = imPacket.getBody();
		if (body != null) {
			String str = new String(body, MessagePacket.CHARSET);
			log.info("接收到消息： {}", str);

			MessageDTO messageDTO = JsonTool.parse(str, MessageDTO.class);
			assert messageDTO != null;
			String path = messageDTO.getPath();

			DispatcherContext context = new DispatcherContext();
			context.setChannelContext(channelContext);
			context.setData(messageDTO.getData());
			Object data = tioDispatcher.invoke(path, context);

			MessagePacket resp = new MessagePacket();
			String respBody = JsonTool.toJSONString(data);
			if (respBody != null) {
				resp.setBody(respBody.getBytes(StandardCharsets.UTF_8));
			}
			Tio.send(channelContext, resp);
		} else {
			log.debug("接收到来自 {} 的心跳消息", channelContext.getClientNode());
		}
	}
}
