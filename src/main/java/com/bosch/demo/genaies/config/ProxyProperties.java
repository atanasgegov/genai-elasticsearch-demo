package com.bosch.demo.genaies.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "proxy")
@Getter
@Setter
public class ProxyProperties {

	private boolean enabled;
	private String host;
	private int port;
	private String protocol;
}