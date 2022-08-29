package com.sven.tio.server.tcp.configuration;

import com.sven.tio.common.props.ServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tio.server.TioServer;
import org.tio.server.TioServerConfig;
import org.tio.server.intf.TioServerHandler;
import org.tio.server.intf.TioServerListener;

import javax.annotation.Resource;

/**
 * @author qinglinl
 * Created on 2022/8/29 2:05 PM
 */
@Configuration
public class TioAutoConfiguration {
	@Value("${spring.application.name}")
	private String appName;
	@Resource
	private ServerConfig serverConfig;

	@Bean
	public TioServerConfig tioServerConfig(TioServerHandler tioServerHandler,
										   TioServerListener tioServerListener) {
		TioServerConfig config = new TioServerConfig(appName, tioServerHandler, tioServerListener);
		config.setHeartbeatTimeout(serverConfig.getTimeout());
		return config;
	}

	@Bean
	public TioServer tioServer(TioServerConfig tioServerConfig) {
		return new TioServer(tioServerConfig);
	}
}
