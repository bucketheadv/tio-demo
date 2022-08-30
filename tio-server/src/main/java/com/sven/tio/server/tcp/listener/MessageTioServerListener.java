package com.sven.tio.server.tcp.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.tio.core.ChannelContext;
import org.tio.core.Tio;
import org.tio.core.intf.Packet;
import org.tio.server.intf.TioServerListener;

/**
 * @author qinglinl
 * Created on 2022/8/24 2:28 PM
 */
@Slf4j
@Component
public class MessageTioServerListener implements TioServerListener {
	@Override
	public boolean onHeartbeatTimeout(ChannelContext channelContext, Long interval, int heartbeatTimeoutCount) {
		log.info("心跳包超时，clientNode: {}, interval: {}, 心跳超时数: {}", channelContext.getClientNode(), interval, heartbeatTimeoutCount);
		return false;
	}

	@Override
	public void onAfterConnected(ChannelContext channelContext, boolean isConnected, boolean isReconnect) throws Exception {
		log.info("客户端 {} 连接上来了, 是否连接: {}, 是否是主动重连: {}", channelContext.getClientNode(), isConnected, isReconnect);
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
		log.info("消息处理完毕, 耗时: {}ms", cost);
	}

	@Override
	public void onBeforeClose(ChannelContext channelContext, Throwable throwable, String remark, boolean isRemove) throws Exception {
		Tio.unbindUser(channelContext);
		Tio.unbindGroup(channelContext);
		log.info("客户端节点关闭: {}, remark: {}, isRemove: {}", channelContext.getClientNode(), remark, isRemove);
	}
}
