package com.sven.tio.client.tcp.configuration;

import com.sven.tio.common.props.ServerConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.tio.client.ReconnConf;
import org.tio.client.TioClient;
import org.tio.client.TioClientConfig;
import org.tio.client.intf.TioClientHandler;
import org.tio.client.intf.TioClientListener;
import org.tio.core.Node;

import java.io.IOException;

/**
 * @author qinglinl
 * Created on 2022/8/29 2:36 PM
 */
@Configuration
public class TioClientConfiguration {
	@Bean
	public ReconnConf reconnConf(ServerConfig serverConfig) {
		return new ReconnConf(serverConfig.getTimeout());
	}

	@Bean
	public TioClientConfig tioClientConfig(TioClientHandler tioClientHandler,
										   TioClientListener tioClientListener,
										   ReconnConf reconnConf,
										   ServerConfig serverConfig) {
		TioClientConfig config = new TioClientConfig(tioClientHandler, tioClientListener, reconnConf);
		config.setHeartbeatTimeout(serverConfig.getTimeout());
		return config;
	}

	@Bean
	public Node serverNode(ServerConfig serverConfig) {
		return new Node(serverConfig.getServer(), serverConfig.getPort());
	}

	@Bean
	public TioClient tioClient(TioClientConfig tioClientConfig) throws IOException {
		return new TioClient(tioClientConfig);
	}
}
