package com.bosch.demo.genaies.service.es;

import org.elasticsearch.client.RestClient;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bosch.demo.genaies.exception.AIException;
import com.bosch.demo.genaies.model.InOutData;
import com.bosch.demo.genaies.model.OutputData;
import com.bosch.demo.genaies.prompt.AIPrompt;
import com.bosch.demo.genaies.prompt.SpringAIPrompt;
import com.bosch.demo.genaies.request.ClusterHealthRequest;
import com.bosch.demo.genaies.request.HotThreadRequest;
import com.bosch.demo.genaies.request.NodesStatsRequest;
import com.bosch.demo.genaies.service.RequestAnalyzerAIAssistant;
import com.google.gson.JsonObject;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import lombok.extern.slf4j.Slf4j;

/**
 * Service class for interacting with Elasticsearch to check the health of the cluster.
 * This class utilizes AI prompts to analyze the health of the Elasticsearch cluster,
 * including hot threads, cluster health, and node statistics.
 */
@Service("elasticsearchHealthAssistant")
@Slf4j
public class ElasticsearchHealthAssistant {

	private ChatClient chatClient;
	private RestClient restClient;
	private ElasticsearchClient elasticsearchClient;

	/**
     * Constructs an instance of ElasticsearchHealthAssistant with the specified dependencies.
     *
     * @param chatClient          the ChatClient for AI interactions
     * @param restClient          the RestClient for making REST calls
     * @param elasticsearchClient  the ElasticsearchClient for interacting with Elasticsearch
     */
	@Autowired
	public ElasticsearchHealthAssistant(ChatClient chatClient, RestClient restClient, ElasticsearchClient elasticsearchClient) {
		this.chatClient = chatClient;
		this.restClient = restClient;
		this.elasticsearchClient = elasticsearchClient;
	}
	
	public OutputData checkClusterHealth() {

		SpringAIPrompt aiPrompt = SpringAIPrompt.builder()
									.chatClient(chatClient)
									.build();
		return this.checkClusterHealth(aiPrompt);
    }
	
	/**
     * Performs the health check of the Elasticsearch cluster using the specified AI prompt.
     *
     * @param aiPrompt - the AI prompt to be used for the health check.
     * @return an OutputData object containing the results of the health check.
     */
	private OutputData checkClusterHealth(AIPrompt aiPrompt) {

		InOutData hotThreadsData = null;
		InOutData clusterHealthData = null;
		InOutData nodeStatsData = null;
		try {
			hotThreadsData = RequestAnalyzerAIAssistant.<JsonObject>builder()
											.request(HotThreadRequest.builder().restClient(restClient).build())
											.prompt(aiPrompt)
											.build().execute();

			clusterHealthData = RequestAnalyzerAIAssistant.<JsonObject>builder()
											.request(ClusterHealthRequest.builder().elasticsearchClient(elasticsearchClient).build())
											.prompt(aiPrompt)
											.build().execute();
	
			nodeStatsData = RequestAnalyzerAIAssistant.<JsonObject>builder()
										.request(NodesStatsRequest.builder().elasticsearchClient(elasticsearchClient).build())
										.prompt(aiPrompt)
										.build().execute();
		} catch(AIException e) {

			log.error("AI Exception: ", e);
			return OutputData.builder()
					.output("Something wrong happened trying to get AI data.")
					.build();
		}

		String message = String.format("AI Checks: %s Hot Threads : %s, %s Cluster Health: %s, %s Node Stats: %s", System.lineSeparator(), hotThreadsData, System.lineSeparator(), clusterHealthData, System.lineSeparator(), nodeStatsData);
		
		return OutputData.builder()
				.output(message)
				.build();
    }
}