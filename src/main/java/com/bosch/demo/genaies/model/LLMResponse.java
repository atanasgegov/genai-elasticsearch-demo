package com.bosch.demo.genaies.model;

import lombok.Data;

@Data
public class LLMResponse {

	private String nameOfTheElasticsearchRequest;
    private String formattedElasticsearchResponse;
    private String analysisAndSuggestions;
}
