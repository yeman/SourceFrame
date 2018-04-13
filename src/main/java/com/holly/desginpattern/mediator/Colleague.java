package com.holly.desginpattern.mediator;

//交流者只知道中介者
public abstract class Colleague {
	private Mediator mediator;

	public Colleague(Mediator mediator) {
		this.mediator = mediator;
	}

	public void sendMessage(String message) {
		mediator.sendMessage(message, this);
	}

	public abstract void notify(String message);
}
