package com.bosch.demo.genaies.prompt;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;

import com.bosch.demo.genaies.model.InOutData;

import lombok.Builder;

@Builder
public class SpringAIPrompt implements AIPrompt {

	private ChatClient chatClient;

	public InOutData call(PromptContext promptContext) {

		Map<String, Object> params = Map.of(
			    "addOnRequest", promptContext.getAddOnRequest(),
			    "addOnOutput", promptContext.getAddOnOutput()
			);

		var response = chatClient.prompt()
                .user(up -> up.params(params))
                .call();
                //.entity(LLMResponse.class);
        
		String genAiInput = String.format("%s", params);
		String genAiOutput = String.format("%s", response.content());
		//String genAiOutput = String.format("%s", response.toString());
		
        
		return new InOutData(genAiInput, genAiOutput);
    }
}
