package com.bosch.demo.genaies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.bosch.demo.genaies.config.ProxyProperties;
import com.bosch.demo.genaies.model.OutputData;
import com.bosch.demo.genaies.service.es.ElasticsearchHealthAssistant;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class GenAIElasticsearchDemoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext app = SpringApplication.run(GenAIElasticsearchDemoApplication.class, args);

		ProxyProperties proxySettings = app.getBean(ProxyProperties.class);
		System.setProperty("https.proxyHost", proxySettings.getHost());
		System.setProperty("https.proxyPort", String.valueOf(proxySettings.getPort()));

		ElasticsearchHealthAssistant elasticsearchHealthAssistant = app.getBean(ElasticsearchHealthAssistant.class);
		OutputData output = elasticsearchHealthAssistant.checkClusterHealth();
		log.info(output.getOutput());
	}
}
