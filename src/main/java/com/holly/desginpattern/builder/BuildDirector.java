package com.holly.desginpattern.builder;

//指挥者类
public class BuildDirector {

	public void BuildCar(CarBuilder builder) {
		builder.procutSteerWheel();
		builder.productWheel();
		builder.productEngine();
	}
}
