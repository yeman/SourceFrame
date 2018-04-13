package com.holly.desginpattern.builder;

public interface CarBuilder {
	
	public void productWheel();

	public void procutSteerWheel();

	public void productEngine();

	public CarProduct getResult();
}
