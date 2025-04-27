package com.bosch.demo.genaies.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Configuration
@ConfigurationProperties(prefix = "spring.ai.prompts.chat")
@Getter
@Setter
public class ChatPromptProperties {
	
    private String system;
    private String user;
}
