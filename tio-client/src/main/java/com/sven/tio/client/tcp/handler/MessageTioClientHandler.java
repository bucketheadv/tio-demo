package com.sven.tio.client.tcp.handler;

import com.sven.tio.common.packet.MessagePacket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.client.intf.TioClientHandler;
import org.tio.core.ChannelContext;
import org.tio.core.TioConfig;
import org.tio.core.exception.TioDecodeException;
import org.tio.core.intf.Packet;

import java.nio.ByteBuffer;

/**
 * @author qinglinl
 * Created on 2022/8/24 1:42 PM
 */
@Slf4j
@Component
public class MessageTioClientHandler implements TioClientHandler {
	private static final MessagePacket heartbeatPacket = new MessagePacket();

	@Override
	public Packet heartbeatPacket(ChannelContext channelContext) {
		return heartbeatPacket;
	}

	@Override
	public Packet decode(ByteBuffer buffer, int limit, int position, int readableLength, ChannelContext channelContext) throws TioDecodeException {
		if (readableLength < MessagePacket.HEADER_LENGTH) {
			return null;
		}
		int bodyLength = buffer.getInt();

		if (bodyLength < 0) {
			throw new TioDecodeException("bodyLength [" + bodyLength + "] is not right, remote: " + channelContext.getClientNode());
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
		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
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
		MessagePacket messagePacket = (MessagePacket) packet;
		byte[] body = messagePacket.getBody();
		if (body != null) {
			String str = new String(body, MessagePacket.CHARSET);
			log.info("收到消息: {}", str);
		}
	}
}
