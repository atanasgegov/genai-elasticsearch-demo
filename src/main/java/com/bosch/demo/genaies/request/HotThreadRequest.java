package com.bosch.demo.genaies.request;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;

import com.bosch.demo.genaies.model.Request;
import com.google.gson.JsonObject;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public class HotThreadRequest implements Request<JsonObject>{

	private static final String JSON_PROPERTY_STATUS = "status";
	
	private RestClient restClient;
	
	@Override
	public JsonObject execute() {
    	JsonObject result = new JsonObject();
    	try {
    		org.elasticsearch.client.Request request = new org.elasticsearch.client.Request("GET", "/_nodes/hot_threads");
            Response response = restClient.performRequest(request);
            StringBuilder hotThreads = new StringBuilder();

            // Read response as plain text
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = reader.readLine()) != null) {
                	hotThreads.append(line).append(System.lineSeparator());
                }
            }

        	result.addProperty("hotThreads", hotThreads.toString());
        } catch (Exception e) {
        	
        	result.addProperty(JSON_PROPERTY_STATUS, "Something wrong happened trying to get hot threads.");
            
        	log.error(result.toString(), e);
        }

    	return result;
	}
	
	@Override
	public String getName() {
		return "GET /_nodes/hot_threads";
	}
}
