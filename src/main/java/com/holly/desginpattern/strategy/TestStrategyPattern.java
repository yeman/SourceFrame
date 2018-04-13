package com.holly.desginpattern.strategy;

public class TestStrategyPattern {
	public static void main(String[] args) {
		Context context= new Context(new ConcreateStrategyA());
		context.contextInterface();
		context = new Context(new ConcreateStrategyB());
		context.contextInterface();
	}
}
