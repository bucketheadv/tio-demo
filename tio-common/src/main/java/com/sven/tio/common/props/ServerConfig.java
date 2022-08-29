package com.sven.tio.common.props;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author qinglinl
 * Created on 2022/8/29 2:13 PM
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "tio")
public class ServerConfig {
	private String server = "127.0.0.1";

	private int port = 6789;

	private int timeout = 5000;
}
