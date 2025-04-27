package com.bosch.demo.genaies.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestClientBuilder.HttpClientConfigCallback;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.Getter;

@Configuration
@Getter
public class BaseConfig {
	
	@Autowired
	private ProxyProperties proxySettings;
	
	@Autowired
	private ElasticsearchConfig elasticsearchConfig;
	
	@Autowired
	private ChatPromptProperties chatPromptProperties;
	
	@Bean
	public ChatClient getChatClient(ChatClient.Builder builder) {
		return builder
				.defaultSystem(chatPromptProperties.getSystem())
				.defaultUser(chatPromptProperties.getUser())
				.build();
	}
	
	@Bean
	public RestClientBuilder getRestClientBuilder() {

        HttpClientConfigCallback clientcallback = httpClientBuilder -> {
            if (proxySettings.isEnabled()) {
                httpClientBuilder.setProxy(new HttpHost(proxySettings.getHost(), proxySettings.getPort(), proxySettings.getProtocol()));
            }

            if (elasticsearchConfig.getUser() != null && elasticsearchConfig.getPassword() != null) {
                CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
                credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticsearchConfig.getUser(), elasticsearchConfig.getPassword()));
                httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
            }

            return httpClientBuilder;
        };

        return RestClient
                .builder(new HttpHost(elasticsearchConfig.getHost(), elasticsearchConfig.getPort(), elasticsearchConfig.getProtocol()))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder.setConnectTimeout(elasticsearchConfig.getSocketTimeout()).setSocketTimeout(elasticsearchConfig.getSocketTimeout()))
                .setHttpClientConfigCallback(clientcallback);
	}
	
	@Bean
    public ElasticsearchTransport getElasticsearchTransport() {

        final ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return new RestClientTransport(this.getRestClientBuilder().build(), new JacksonJsonpMapper(mapper));
    }

	@Bean
	public ElasticsearchClient getElasticsearchClient(ElasticsearchTransport transport) {
		return new ElasticsearchClient(transport);
	}

	@Bean
	public ElasticsearchAsyncClient getElasticsearchAsyncClient(ElasticsearchTransport transport) {
		return new ElasticsearchAsyncClient(transport);
	}
}