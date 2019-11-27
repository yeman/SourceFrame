package com.holly.desginpattern.singleton;

public class LazySingleton {
	private static int count;
	private static LazySingleton lazySingleton = null;

	private LazySingleton() {
		System.out.println(Thread.currentThread().getName()+"\t"+"LazySingleton 私有的构造方法被实例化 " + (++count) + " 次。");
	}

	public static LazySingleton getInstance() {
		if (lazySingleton == null) {
			lazySingleton = new LazySingleton();
		}
		return lazySingleton;
	}
	

}

