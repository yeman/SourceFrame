package com.holly.desginpattern.prototype;

public class ConcreatePrototype extends Prototype {
	private String count;
	public void show(String count) {
		System.out.println("原型类实现类"+count);
	}
}
