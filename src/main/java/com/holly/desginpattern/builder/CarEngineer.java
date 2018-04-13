package com.holly.desginpattern.builder;

public class CarEngineer implements CarBuilder {
	private CarProduct car = new CarProduct();

	@Override
	public void productWheel() {
		car.buildWheel("宝马轮子");

	}

	@Override
	public void procutSteerWheel() {
		car.buildSteerWheel("宝马方向盘");

	}

	@Override
	public void productEngine() {
		car.buildEngine("宝马引擎");

	}

	@Override
	public CarProduct getResult() {
		return car;
	}

}
