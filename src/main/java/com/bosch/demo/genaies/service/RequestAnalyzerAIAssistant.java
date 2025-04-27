package com.bosch.demo.genaies.service;

import com.bosch.demo.genaies.exception.AIException;
import com.bosch.demo.genaies.model.InOutData;
import com.bosch.demo.genaies.model.Request;
import com.bosch.demo.genaies.prompt.AIPrompt;
import com.bosch.demo.genaies.prompt.PromptContext;

import lombok.Builder;

@Builder
public class RequestAnalyzerAIAssistant<R> {

	private AIPrompt prompt;
	private Request<R> request;

    public InOutData execute() throws AIException {

		R addOnResponse = request.execute();

		PromptContext context = PromptContext.builder()
				.addOnRequest(request.getName())
				.addOnOutput(addOnResponse.toString())
				.build();

		return prompt.call( context );
	}
}