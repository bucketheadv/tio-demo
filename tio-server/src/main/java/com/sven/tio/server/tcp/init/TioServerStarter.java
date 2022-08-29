package com.sven.tio.server.tcp.init;

import com.sven.tio.common.props.ServerConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.tio.server.TioServer;

import javax.annotation.Resource;

/**
 * @author qinglinl
 * Created on 2022/8/24 1:31 PM
 */
@Component
public class TioServerStarter implements CommandLineRunner {
	@Resource
	private ServerConfig serverConfig;
	@Resource
	private TioServer tioServer;

	@Override
	public void run(String... args) throws Exception {
		tioServer.start(serverConfig.getServer(), serverConfig.getPort());
	}
}
