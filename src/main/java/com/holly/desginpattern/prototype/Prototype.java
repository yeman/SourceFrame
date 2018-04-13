package com.holly.desginpattern.prototype;

//原型设计模式
public class Prototype implements Cloneable {
	
	public Prototype clone() {
		Prototype prototype = null;
		try {
			prototype = (Prototype) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return prototype;
	}
}
