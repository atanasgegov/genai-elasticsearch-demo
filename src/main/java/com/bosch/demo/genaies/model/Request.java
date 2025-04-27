package com.bosch.demo.genaies.model;

public interface Request<R> {

	public R execute();
	
	public String getName();
}
