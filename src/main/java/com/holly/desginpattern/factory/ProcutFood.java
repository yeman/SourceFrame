package com.holly.desginpattern.factory;

public class ProcutFood extends Food{
	
	public ProcutFood(){
		System.out.println("创建 ProcutFood");
	}

	@Override
	public void eat() {
		System.out.println("ProcutFood eat ");
	}

}
