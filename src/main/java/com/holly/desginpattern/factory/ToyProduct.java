package com.holly.desginpattern.factory;

public class ToyProduct extends Product{
	public ToyProduct(){
		System.out.println("创建 ToyProduct");
	}
	@Override
	public void use() {
		System.out.println("使用ToyProduct");
		
	}
	
}
