package com.bosch.demo.genaies.prompt;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class PromptContext {

	private String addOnRequest;
	private String addOnOutput;
}
