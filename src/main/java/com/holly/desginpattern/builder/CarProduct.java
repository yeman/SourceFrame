package com.holly.desginpattern.builder;

public class CarProduct {
	// 为了丰富属性
	private String wheel;
	private String steerWheel;
	private String engine;

	public void buildWheel(String wheel) {
		this.wheel = wheel;
		System.out.println("build wheel ...");

	}

	public void buildSteerWheel(String steerWheel) {
		this.steerWheel = steerWheel;
		System.out.println("build steer wheel ...");

	}

	public void buildEngine(String engine) {
		this.engine = engine;
		System.out.println("build engine ...");
	}
	
	@Override
	public String toString() {
		return "CarProduct [wheel=" + wheel + ", steerWheel=" + steerWheel + ", engine=" + engine + "]";
	}

	public void show() {
		System.out.println(this.toString());
	}

}
