package com.holly.desginpattern.mediator;

//为Colleague提供抽象交流类
public abstract class Mediator {
	public abstract void sendMessage(String message,Colleague colleague);
}
