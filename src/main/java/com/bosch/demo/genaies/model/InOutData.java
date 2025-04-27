package com.bosch.demo.genaies.model;

public record InOutData(String input, String output) {
	
	public static final String INFO_OUT_MESSAGE = "---> ";
	public static final String INFO_IN_MESSAGE = "<--- ";

	@Override
	public String toString() {
		return INFO_IN_MESSAGE
				+ System.lineSeparator()
				+ input 
				+ System.lineSeparator() 
				+ INFO_OUT_MESSAGE
				+ System.lineSeparator()
				+ output;
	}
}