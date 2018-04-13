package com.holly.desginpattern.observer;

public abstract class OberServer {
	protected String name;
	protected Subject subject;
	OberServer(String name,Subject subject){
		this.name = name;
		this.subject = subject;
	}
	public abstract void update();

}
