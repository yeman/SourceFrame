package com.holly.desginpattern.factory;

public class TestFacotryMethod {
	public static void main(String[] args) {
		IProductCreator creator = new ProductCreator();
		Product product = creator.factoryMethod();
		product.use();
	}
}
