package com.holly.desginpattern.factory;

public class AbstractFactoryImpl extends AbstractFactory{

	@Override
	public Food createFood() {
		return new ProcutFood();
	}

	@Override
	public Product createToy() {
		return new ToyProduct();
	}

}
