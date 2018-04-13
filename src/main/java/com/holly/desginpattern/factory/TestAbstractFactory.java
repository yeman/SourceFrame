package com.holly.desginpattern.factory;

public class TestAbstractFactory {
	public static void main(String[] args) {
		AbstractFactory abstractFactory = new AbstractFactoryImpl();
		abstractFactory.createFood().eat();
		abstractFactory.createToy().use();
	}
}
