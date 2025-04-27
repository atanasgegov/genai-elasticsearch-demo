package com.bosch.demo.genaies.prompt;

import com.bosch.demo.genaies.exception.AIException;
import com.bosch.demo.genaies.model.InOutData;

public interface AIPrompt {
	
	public InOutData call(PromptContext promptContext) throws AIException;

}
