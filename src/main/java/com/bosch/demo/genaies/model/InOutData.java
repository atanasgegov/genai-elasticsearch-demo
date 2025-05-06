package com.bosch.demo.genaies.model;

public record InOutData(String input, String output) {
	
	public static final String INFO_INPUT_MESSAGE = "-----> ";
	public static final String INFO_OUTPUT_MESSAGE = "<----- ";
	public static final String INFO_DELIMITER_MESSAGE = "------";
	public static final String INFO_END_MESSAGE = "##";
	
	@Override
	public String toString() {
		return INFO_INPUT_MESSAGE + System.lineSeparator()
				+ input + System.lineSeparator()
				+ INFO_DELIMITER_MESSAGE + System.lineSeparator() 
				+ INFO_OUTPUT_MESSAGE + System.lineSeparator()
				+ output
				+ INFO_END_MESSAGE;
	}
}