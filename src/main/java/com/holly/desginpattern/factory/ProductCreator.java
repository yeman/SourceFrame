package com.holly.desginpattern.factory;

public class ProductCreator implements IProductCreator {

	@Override
	public Product factoryMethod() {
		
		return new ToyProduct();
	}

}
