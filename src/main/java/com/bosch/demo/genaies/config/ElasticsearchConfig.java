package com.bosch.demo.genaies.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Getter
@Setter
public class ElasticsearchConfig {

	private String host;
	private int port;
	private String protocol;
	private int socketTimeout;
	private String user;
	private String password;
}
