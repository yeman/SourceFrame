package com.holly.desginpattern.singleton;

public class HungerSingleton {
	private static int count;

	private static HungerSingleton hungerSingleton = new HungerSingleton();

	private HungerSingleton() {
		System.out.println(Thread.currentThread().getName()+"\t"+"HungerSingleton 私有构造器执行" + (++count));
	}

	public static HungerSingleton getInstance() {
		return hungerSingleton;
	}
}
