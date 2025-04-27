package com.bosch.demo.genaies.exception;

public class AIException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public AIException(String message) {
		super(message);
	}
	
	public AIException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public AIException(Throwable cause) {
		super(cause);
	}

}
