package com.holly.desginpattern.chain;

import java.util.HashSet;
import java.util.Set;

public class TestChain {
	public static void main(String[] args) {
		Handler handlerA = new HandlerA();
		Handler handlerB = new HandlerB();
		Handler handlerC = new HandlerC();
		handlerA.setProcessor(handlerB);
		handlerB.setProcessor(handlerC);
		
		Set<Handler> set = new HashSet<Handler>();
		set.add(new HandlerC());
		set.add(new HandlerB());
		set.add(new HandlerA());
		for(Handler h : set){
			System.out.println(">> current"+h.getClass().getSimpleName());
			handlerA.chain(h);
		}
	}
}
