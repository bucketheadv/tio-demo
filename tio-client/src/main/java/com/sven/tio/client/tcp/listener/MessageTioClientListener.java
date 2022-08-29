package com.sven.tio.client.tcp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.client.intf.TioClientListener;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @author qinglinl
 * Created on 2022/8/24 2:23 PM
 */
@Slf4j
@Component
public class MessageTioClientListener implements TioClientListener {
	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
		log.info("连接 {} 结果 {}, 是否是主动重连 {}", channelContext.getClientNode(), isConnected, isReconnect);
	}

	@Override
	public void onAfterDecoded(ChannelContext channelContext, Packet packet, int packetSize) throws Exception {

	}

	@Override
	public void onAfterReceivedBytes(ChannelContext channelContext, int receivedBytes) throws Exception {

	}

	@Override
	public void onAfterSent(ChannelContext channelContext, Packet packet, boolean isSentSuccess) throws Exception {

	}

	@Override
	public void onAfterHandled(ChannelContext channelContext, Packet packet, long cost) throws Exception {

	}

	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
		log.info("节点 {} 即将关闭, remark: {}, isRemove: {}", channelContext.getClientNode(), remark, isRemove);
	}
}
