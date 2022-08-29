package com.sven.tio.dispatcher.spring.starter.dispatch.context;

import lombok.Data;
import org.tio.core.ChannelContext;

/**
 * @author qinglinl
 * Created on 2022/8/29 1:47 PM
 */
@Data
public class DispatcherContext {
	private Object data;

	private ChannelContext channelContext;
}
